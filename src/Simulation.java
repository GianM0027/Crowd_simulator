import models.*;
import support.dataHandler.DataHandler;
import support.Support;
import support.constants.Constant;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * This class has only one instance at a time, it contains every information about the Simulation and its components.
 * This class also extends JPanel because the class itself is the panel that contains the animation
 * */
public class Simulation extends JPanel{
    private static Simulation instance;
    private Animation animation;
    private boolean isRunning;

    private ArrayList<Pedestrian> crowd;
    private ArrayList<Group> groups;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<WayPoint> wayPoints;
    private ArrayList<WayPoint> restingPoints;
    private Building building;
    private int numberOfPeople;
    private int numberOfGroups;
    private int minGroupSize;
    private int maxGroupSize;
    private int numberOfObstacles;
    private int numberOfWayPoints;

    //collect data parameters
    private javax.swing.Timer collectDataTimer;
    private int timestampNum;


    public Simulation(){
        this.isRunning = false;
        this.minGroupSize = Constant.MIN_GROUPS_SIZE;
        this.maxGroupSize = Constant.MAX_GROUPS_SIZE;
        this.numberOfObstacles = 0;
        this.numberOfPeople = 0;
        this.numberOfWayPoints = 0;
        this.timestampNum = 0;
    }


    /**
     * only one instance of the simulation at a time
     * */
    public static Simulation getInstance(){
        if(instance == null)
            instance = new Simulation();
        return instance;
    }


    /*!**************************************    SIMULATION CONTROLS    *****************************************/
    /**
     * When called, this function start the animation given the parameters saved as attributes of the class
     * */
    protected void startSimulation() throws IOException {
        this.isRunning = true; //set the attribute

        //creating the entities
        this.building = new Building(this.getWidth(), this.getHeight());
        createObstacles();
        createWayPoints();
        createCrowd();
        this.building.setWayPoints(wayPoints);

        //setting the Active Entities Panel with all its tabs
        ActiveEntitiesPanel.getInstance().setObstaclesTab();
        ActiveEntitiesPanel.getInstance().setWayPointsTab();
        ActiveEntitiesPanel.getInstance().setPedestriansTab(this.crowd);
        ActiveEntitiesPanel.getInstance().enableFilters();
        ActiveEntitiesPanel.getInstance().updateFilteredCrowd();
        ActiveEntitiesPanel.getInstance().getRefresh().start();

        //"cleaning the panel" and Initializing the animation
        this.removeAll();
        this.revalidate();
        this.repaint();
        this.animation = new Animation(this, this.crowd, this.obstacles, this.wayPoints, this.groups, this.building);
        this.add(animation);
        this.revalidate();
        this.repaint();

        //collecting static data from obstacles, groups and waypoints
        DataHandler dataHandler = new DataHandler();
        dataHandler.setObstaclesData(obstacles);
        dataHandler.setWaypointsData(wayPoints);
        dataHandler.setGroupsStaticData(groups);
        dataHandler.setPedestriansStaticData(crowd);

        //start the data collector
        collectDataTimer = new Timer(1000, e -> {
            try {
                collectData(dataHandler);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        collectDataTimer.start();
    }

    /**
     * This function pauses the timers of the simulation
     * */
    protected void pauseSimulation(){
        this.animation.pause();
        this.collectDataTimer.stop();
        ActiveEntitiesPanel.getInstance().getRefresh().stop();

        for(Group group : this.groups)
            if(!group.isStartWalking())
                group.getStartWalkingTimer().stop();

        for(Pedestrian pedestrian : crowd)
            pedestrian.stopWasteEnergyTimer();
    }

    /**
     * This function restarts every Timer of the simulation
     */
    protected void resumeSimulation(){
        this.animation.resume();
        this.collectDataTimer.start();
        ActiveEntitiesPanel.getInstance().getRefresh().start();

        for(Group group : this.groups)
            if(!group.isStartWalking())
                group.getStartWalkingTimer().start();

        for(Pedestrian pedestrian : crowd)
            pedestrian.startWasteEnergyTimer();
    }


    /**
     * resetting parameters of the simulation, attributes of the class and resetting active entities panel
     * */
    protected void stopSimulation(){
        setParameters(0,Constant.MIN_GROUPS_SIZE,Constant.MAX_GROUPS_SIZE,0, 0);
        this.groups.clear();
        this.wayPoints.clear();
        this.obstacles.clear();
        this.crowd.clear();
        ActiveEntitiesPanel.getInstance().setObstaclesTab();
        ActiveEntitiesPanel.getInstance().setWayPointsTab();
        ActiveEntitiesPanel.getInstance().setFiltersTab();
        ActiveEntitiesPanel.getInstance().disableFilters();
        ActiveEntitiesPanel.getInstance().setPedestriansTab(this.crowd);

        for(Pedestrian pedestrian : crowd)
            pedestrian.stopWasteEnergyTimer();

        ActiveEntitiesPanel.getInstance().getRefresh().stop();

        this.isRunning = false;
        this.removeAll();
        this.revalidate();
        this.repaint();
    }

    /**
     * This function is called cyclically, every time it checks if the simulation is still running. If it is running
     * it collects information about dynamic entities, otherwise it stops the timer that calls this function and it creates
     * the final file with all the information about the simulation
     * @param dataHandler object of the class dataHandler
     * @throws IOException
     */
    private void collectData(DataHandler dataHandler) throws IOException {
        //if the simulation ends, set the file with all the data
        if(!this.isRunning){
            dataHandler.simulationDataToJSON();
            this.collectDataTimer.stop();
        }
        else{
            dataHandler.setPedestriansTimestamp(crowd, timestampNum);
            dataHandler.setGroupsTimestamp(groups, timestampNum);
            timestampNum++;
        }
    }


    /*!*************************************     ENTITIES CREATION     *****************************************/
    /**
     * This functions, given the number of obstacles inserted by the user, create a list of obstacles
     * */
    private void createObstacles(){
        this.obstacles = new ArrayList<>();
        Obstacle o;

        //Until the obstacle is generated in a forbidden position it is repositioned
        for(int i = 0; i < this.numberOfObstacles; i++) {
            do {
                Point2D point = new Point2D.Double(Support.getRandomValue(Constant.BOUNDS_DISTANCE + Constant.BUILDING_STROKE + Constant.BUILDING_DISTANCE_LEFT,
                        this.getWidth() - Constant.BUILDING_DISTANCE_RIGHT - Constant.OBSTACLE_WIDTH - 2 * Constant.BOUNDS_DISTANCE - 1),
                        Support.getRandomValue(Constant.BOUNDS_DISTANCE + Constant.BUILDING_DISTANCE_UP_DOWN + Constant.BUILDING_STROKE,
                                this.getHeight() - Constant.BUILDING_DISTANCE_UP_DOWN - Constant.BUILDING_STROKE - Constant.OBSTACLE_WIDTH - 2 * Constant.BOUNDS_DISTANCE - 1));

                o = new Obstacle(point);
            }while (!building.distanceIsEnough(o) || building.checkCollisionOnDoorFreeSpace(o) || checkCollisionOnOtherEntities(o));

            this.obstacles.add(i, o); //save list of obstacles as an attribute
        }

        Support.sortObstacles(this.obstacles);
    }

    /**
     * This functions, given the number of waypoints inserted by the user, create a list of obstacles
     * */
    private void createWayPoints(){
        this.wayPoints = new ArrayList<>();
        WayPoint w;

        for(int i = 0; i < this.numberOfWayPoints; i++){
            do {
                Point2D point = new Point2D.Double(Support.getRandomValue(Constant.BOUNDS_DISTANCE + Constant.BUILDING_STROKE + Constant.BUILDING_DISTANCE_LEFT,
                        this.getWidth() - Constant.BUILDING_DISTANCE_RIGHT - Constant.WAYPOINT_WIDTH - 2 * Constant.BOUNDS_DISTANCE - 1),
                        Support.getRandomValue(Constant.BOUNDS_DISTANCE + Constant.BUILDING_DISTANCE_UP_DOWN + Constant.BUILDING_STROKE,
                                this.getHeight() - Constant.BUILDING_DISTANCE_UP_DOWN - Constant.BUILDING_STROKE - Constant.WAYPOINT_WIDTH - 2 * Constant.BOUNDS_DISTANCE - 1));

                w = new WayPoint(point);
                w.setWaypointID(i);
            }while (!building.distanceIsEnough(w) || building.checkCollisionOnDoorFreeSpace(w) || checkCollisionOnOtherEntities(w));

            this.wayPoints.add(i, w);
        }
        Support.sortWayPoints(this.wayPoints);

        this.restingPoints = new ArrayList<>(wayPoints);
        restingPoints.removeIf(wayPoint -> !wayPoint.isRestingArea());
    }

    /**
     * This functions, given the parameters inserted by the user, create the crowd and the groups contained in it. Giving them the knowledge
     * about themselves and the environment
     * */
    private void createCrowd(){
        this.crowd = new ArrayList<>();
        this.groups = new ArrayList<>();

        //create the crowd
        for(int i = 0; i < numberOfPeople; i++){
            Point2D point = new Point2D.Double(Support.getRandomValue(Constant.PEDESTRIAN_WIDTH + Constant.BOUNDS_DISTANCE,
                    Constant.BUILDING_DISTANCE_LEFT - Constant.PEDESTRIAN_WIDTH - 2*Constant.BOUNDS_DISTANCE),
                    Support.getRandomValue(Constant.PEDESTRIAN_WIDTH + Constant.BOUNDS_DISTANCE,
                            this.getHeight() - Constant.PEDESTRIAN_WIDTH - Constant.BOUNDS_DISTANCE));

            Pedestrian p = new Pedestrian(point, i, building);
            this.crowd.add(i, p);
        }

        //divide the crowd into groups
        ArrayList<Pedestrian> people = new ArrayList<>(crowd);
        int groupIndex = 0;
        int groupSize;

        do {
            groupSize = Support.getRandomValue(minGroupSize, maxGroupSize);
            List<WayPoint> goalsList = new ArrayList<>(goalsList(groupSize));
            ArrayList<Pedestrian> peopleInTheGroup = new ArrayList<>();

            for (int i = 0; i < groupSize; i++) {
                if (people.isEmpty())
                    break;
                peopleInTheGroup.add(people.get(0));
                people.remove(0);
            }

            Group group = new Group(groupIndex, goalsList, peopleInTheGroup);
            groupIndex++;

            Color groupColor;
            do {
                groupColor = new Color(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat());
                group.setColor(groupColor);
            }while(groupColor == Color.GRAY || groupColor == Color.WHITE || groupColor == Color.BLACK || groupColor == Color.RED);

            //SET RESTING POINTS
            restingPoints = new ArrayList<>(wayPoints);
            restingPoints.removeIf(w -> !w.isRestingArea());
            group.setRestingPoints(restingPoints);

            groups.add(group);

            //each member of each group knows the members of their group
            for (Pedestrian p : group.getPedestrians()) {
                p.setGroup(group);
                p.setObstacles(obstacles);
            }

        } while (!people.isEmpty());

        numberOfGroups = groups.size();
    }

    /**
     * This function assigns a list of goals to each group, the bigger is the group (with respect to the number of waypoints)
     * the higher is the probability of having more goals
     * */
    private List<WayPoint> goalsList(int groupSize){
        ArrayList<WayPoint> goalsList = new ArrayList<>(this.wayPoints);
        float groupsGoalsRatio = (float)groupSize/goalsList.size();

        //remove all the resting points, they will be adeed dinamilly if necessary
        goalsList.removeIf(WayPoint::isRestingArea);

        //If the group if pedestrian has size lower than 1/20 of the list of goals then 30% of probability to assign each goal
        if(groupsGoalsRatio <= 0.55) {
            goalsList.removeIf(w -> Support.getRandomValue(1, 100) < 70);
        }

        //If the group if pedestrian has size lower than 1/10 of the list of goals then 40% of probability to assign each goal
        else if(groupsGoalsRatio <= 0.1) {
            goalsList.removeIf(w -> Support.getRandomValue(1, 100) < 60);
        }

        //If the group if pedestrian has size lower than 1/5 of the list of goals then 50% of probability to assign each goal
        else if(groupsGoalsRatio <= 0.2) {
            goalsList.removeIf(w -> Support.getRandomValue(1, 100) < 50);
        }

        //If the group if pedestrian has size lower than 1/3 of the list of goals then 60% of probability to assign each goal
        else if(groupsGoalsRatio <= 0.33) {
            goalsList.removeIf(w -> Support.getRandomValue(1, 100) < 40);
        }

        //If the group if pedestrian has size lower than 1/2 of the list of goals then 70% of probability to assign each goal
        else if(groupsGoalsRatio <= 0.5) {
            goalsList.removeIf(w -> Support.getRandomValue(1, 100) < 30);
        }

        //If the group if pedestrian has size higher than half list of goals then 80% of probability to assign each goal
        else if(groupsGoalsRatio < 0.8) {
            goalsList.removeIf(w -> Support.getRandomValue(1, 100) < 20);
        }

        //If the group if pedestrian has size higher than the list of goals then 90% of probability to assign each goal
        else if(groupsGoalsRatio >= 0.8) {
            goalsList.removeIf(w -> Support.getRandomValue(1, 100) < 10);
        }
        return goalsList;
    }

    /**
     * This function is used during the generation phase of the obstacles and it checks whether an obstacle (with its initial random position)
     * is colliding with another one
     * @param o obstacle in the environment
     * @return true if there is a collision, false otherwise
     */
    private boolean checkCollisionOnOtherEntities(Obstacle o){
        for(Obstacle obstacle : obstacles)
            if(o.getEntityBounds().getBoundsRectangle().intersects(obstacle.getEntityBounds().getBoundsRectangle()))
                return true;

        return false;
    }

    /**
     * This function is used during the generation phase of the waypoints and it checks whether a waypoint (with its initial random position)
     * is colliding with another one
     * @param w waypoint in the environment
     * @return true if there is a collision, false otherwise
     */
    private boolean checkCollisionOnOtherEntities(WayPoint w){
        for(Obstacle obstacle : obstacles)
            if(w.getEntityBounds().getBoundsRectangle().intersects(obstacle.getEntityBounds().getBoundsRectangle()))
                return true;
        for(WayPoint wayPoint : wayPoints)
            if(w.getEntityBounds().getBoundsRectangle().intersects(wayPoint.getEntityBounds().getBoundsRectangle()))
                return true;

        return false;
    }

    /**
     * This function is used to receive the parameters inserted by the user in the setting panel, it takes them as
     * parameters and it saves their values as attributes of the class
     * @param numberOfPeople
     * @param minGroupSize
     * @param maxGroupSize
     * @param numberOfObstacles
     * @param numberOfWayPoints
     */
    public void setParameters(int numberOfPeople, int minGroupSize, int maxGroupSize, int numberOfObstacles, int numberOfWayPoints){
        this.minGroupSize = minGroupSize;
        this.maxGroupSize = maxGroupSize;
        this.numberOfObstacles = numberOfObstacles;
        this.numberOfPeople = numberOfPeople;
        this.numberOfWayPoints = numberOfWayPoints;
    }

    /**
     * This function checks if one of the required parameters of the class is zero (in particular, it checks the number of obstacles inserted,
     * the number of people in the crowd and the number of waypoints)
     * @return true if there are missing parameters, false otherwise
     */
    public boolean missingParameters(){
        if (this.numberOfObstacles == 0 || this.numberOfPeople == 0 || this.numberOfWayPoints == 0)
            return true;
        return false;
    }

    /*!**************************************    ACCESSORS    *****************************************/
    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public int getMinSizeOfGroups() {
        return minGroupSize;
    }
    public int getMaxSizeOfGroups() {
        return maxGroupSize;
    }

    public int getNumberOfObstacles() {
        return numberOfObstacles;
    }

    public int getNumberOfWayPoints() {
        return numberOfWayPoints;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public List<Pedestrian> getCrowd() {
        return crowd;
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public int getNumberOfGroups() {
        return numberOfGroups;
    }


    public ArrayList<WayPoint> getWayPoints() {
        return wayPoints;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean running) {
        isRunning = running;
    }

}

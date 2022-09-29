import models.*;
import support.Support;
import support.constants.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

/**
 * Contain all the settings and contents of the actual simulation
 * */
public class Simulation extends JPanel{
    private static Simulation instance;
    private Animation animation;
    private boolean isRunning;

    private ArrayList<Pedestrian> crowd;
    private ArrayList<Group> groups;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<WayPoint> wayPoints;
    private Building building;
    private int numberOfPeople;
    private int numberOfGroups;
    private int minGroupSize;
    private int maxGroupSize;
    private int numberOfObstacles;
    private int numberOfWayPoints;

    public Simulation(){
        this.isRunning = false;
        this.minGroupSize = Constant.MIN_GROUPS_SIZE;
        this.maxGroupSize = Constant.MAX_GROUPS_SIZE;
        this.numberOfObstacles = 0;
        this.numberOfPeople = 0;
        this.numberOfWayPoints = 0;
    }


    /**
     * only one instance of the simulation at a time
     * */
    public static Simulation getInstance(){
        if(instance == null)
            instance = new Simulation();
        return instance;
    }


    /***************************************    SIMULATION CONTROLS    *****************************************/
    /**
     *
     * */
    protected void startSimulation(){
        this.building = new Building(this.getWidth(), this.getHeight());
        createObstacles();
        createWayPoints();
        createCrowd();

        //Printing entities on the "active entities panel"
        ActiveEntitiesPanel.getInstance().setObstaclesTab();
        ActiveEntitiesPanel.getInstance().setWayPointsTab();
        ActiveEntitiesPanel.getInstance().setPedestriansTab(this.crowd);
        ActiveEntitiesPanel.getInstance().enableFilters();
        ActiveEntitiesPanel.getInstance().updateFilteredCrowd();

        this.removeAll();
        this.revalidate();
        this.repaint();
        animation = new Animation(this, this.crowd, this.obstacles, this.wayPoints, this.groups, this.building);
        this.add(animation);
        this.revalidate();
        this.repaint();
    }

    /**
     * Pause the timer that update pedestrian's data
     * */
    protected void pauseSimulation(){
        this.animation.pause();
    }

    protected void resumeSimulation(){
        this.animation.resume();
    }


    /**
     * reset parameters of the simulation
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
        ActiveEntitiesPanel.getInstance().getRefresh().stop();

        for(Pedestrian pedestrian : crowd)
            pedestrian.stopWasteEnergyTimer();

        this.setIsRunning(false);
        this.removeAll();
        this.revalidate();
        this.repaint();
    }


    /***************************************     ENTITIES CREATION     *****************************************/
    /**
     *
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
            }while (building.checkCollision(o) != null || building.checkCollisionOnDoorFreeSpace(o));

            this.obstacles.add(i, o);
        }

        Support.sortObstacles(this.obstacles);
    }

    /**
     *
     * */
    private void createWayPoints(){
        this.wayPoints = new ArrayList<>();
        WayPoint w;
        int id = 0;

        for(int i = 0; i < this.numberOfWayPoints; i++){
            do {
                Point2D point = new Point2D.Double(Support.getRandomValue(Constant.BOUNDS_DISTANCE + Constant.BUILDING_STROKE + Constant.BUILDING_DISTANCE_LEFT,
                        this.getWidth() - Constant.BUILDING_DISTANCE_RIGHT - Constant.WAYPOINT_WIDTH - 2 * Constant.BOUNDS_DISTANCE - 1),
                        Support.getRandomValue(Constant.BOUNDS_DISTANCE + Constant.BUILDING_DISTANCE_UP_DOWN + Constant.BUILDING_STROKE,
                                this.getHeight() - Constant.BUILDING_DISTANCE_UP_DOWN - Constant.BUILDING_STROKE - Constant.WAYPOINT_WIDTH - 2 * Constant.BOUNDS_DISTANCE - 1));

                w = new WayPoint(point);
                w.setWaypointID(id++);
            }while (building.checkCollision(w) != null || building.checkCollisionOnDoorFreeSpace(w));

            this.wayPoints.add(i, w);
        }
        Support.sortWayPoints(this.wayPoints);
    }

    /**
     * Creates crowd and groups
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
     * Assign a list of goals to each group, the bigger is the group (with respect to the number of waypoints)
     * the higher is the probability of having more goals
     * */
    private List<WayPoint> goalsList(int groupSize){
        ArrayList<WayPoint> goalsList = new ArrayList<>(this.wayPoints);
        float groupsGoalsRatio = (float)groupSize/goalsList.size();

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

        //adding the doors through their path as waypoints
        addDoorToGoalsList(goalsList);

        return goalsList;
    }

    private void addDoorToGoalsList(List<WayPoint> goalsList){
        //add exit and entrance of the builing
        goalsList.add(0, building.getEntrance());
        goalsList.add(building.getExit());

        //add "middle goals" (doors between points)
        for(int i = 1; i < goalsList.size(); i++){

            //if previous waypoint was in the hall (the next one could be still in the hall or in a room)
            if(building.getHall().contains(goalsList.get(i-1).getPosition())){
                //if the actual goal is in a room add a door of that room to the goalsList
                for(Room room : building.getRooms()){
                    if(room.getRoomRectangle().contains(goalsList.get(i).getPosition())) {
                        goalsList.add(i, room.getDoor());
                        i++;
                    }
                }
                //else the actual goal is still in the hall
            }

            //if previous waypoint was in a room (the next one could be in the same room, in the hall, in another room)
            else{
                for(Room room : building.getRooms()){
                    if(room.getRoomRectangle().contains(goalsList.get(i-1).getPosition())){
                        //the actual goal can be in the hall
                        if(building.getHall().contains(goalsList.get(i).getPosition())){
                            goalsList.add(i, room.getDoor());
                            i++;
                        }

                        //the actual goal can be in another room
                        for(Room room1 : building.getRooms()){
                            if(room1.getRoomRectangle().contains(goalsList.get(i).getPosition()) && !room.equals(room1)){
                                goalsList.add(i, room1.getDoor());
                                goalsList.add(i, room.getDoor());
                                i+=2;
                            }
                        }

                        //else the actual goal is in the same room as the previous one
                    }
                }
            }
        }
    }

    /***************************************    ACCESSORS    *****************************************/
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

    public void setParameters(int numberOfPeople, int minGroupSize, int maxGroupSize, int numberOfObstacles, int numberOfWayPoints){
        this.minGroupSize = minGroupSize;
        this.maxGroupSize = maxGroupSize;
        this.numberOfObstacles = numberOfObstacles;
        this.numberOfPeople = numberOfPeople;
        this.numberOfWayPoints = numberOfWayPoints;
    }

    public boolean missingParameters(){
        if (this.numberOfObstacles == 0 || this.numberOfPeople == 0 || this.numberOfWayPoints == 0)
            return true;
        return false;
    }

}

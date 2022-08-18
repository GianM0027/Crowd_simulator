import models.Group;
import models.Obstacle;
import models.Pedestrian;
import models.WayPoint;
import support.Support;
import support.constants.Constant;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private ArrayList<WayPoint> crowdGoals;

    private int numberOfPeople;
    private int numberOfGroups;
    private int sizeOfGroups;
    private int numberOfObstacles;
    private int numberOfWayPoints;

    public Simulation(){
        this.isRunning = false;
        this.sizeOfGroups = 0;
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
        animation = new Animation(this, this.crowd, this.obstacles, this.wayPoints, this.groups);
        this.add(animation);
        this.revalidate();
        this.repaint();
    }

    /**
     *
     * */
    protected void pauseSimulation(){
        this.animation.pause();
    }

    protected void resumeSimulation(){
        this.animation.resume();
    }


    /**
     *
     * */
    protected void stopSimulation(){
        setParameters(0,0,0,0);
        this.groups.clear();
        this.wayPoints.clear();
        this.obstacles.clear();
        this.crowd.clear();
        ActiveEntitiesPanel.getInstance().setObstaclesTab();
        ActiveEntitiesPanel.getInstance().setWayPointsTab();
        ActiveEntitiesPanel.getInstance().setFiltersTab();
        ActiveEntitiesPanel.getInstance().disableFilters();
        ActiveEntitiesPanel.getInstance().setPedestriansTab(this.crowd);

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

        for(int i = 0; i < this.numberOfObstacles; i++) {
            Point point = new Point();
            point.x = Support.getRandomValue(Constant.BOUNDS_DISTANCE + Constant.BUILDING_STROKE + Constant.BUILDING_DISTANCE_LEFT,
                    this.getWidth() - Constant.BUILDING_DISTANCE_RIGHT - Constant.ENTITY_SIZE - 2 * Constant.BOUNDS_DISTANCE - 1);
            point.y = Support.getRandomValue(Constant.BOUNDS_DISTANCE + Constant.BUILDING_DISTANCE_UP_DOWN + Constant.BUILDING_STROKE,
                    this.getHeight() - Constant.BUILDING_DISTANCE_UP_DOWN - Constant.BUILDING_STROKE - Constant.ENTITY_SIZE - 2 * Constant.BOUNDS_DISTANCE - 1);

            Obstacle o = new Obstacle(point);
/*
            //no overlapping controls
            if (obstacles.size() > 0){
                for (Obstacle obstacle : obstacles) {
                    while(Support.distance(o.getBounds().getCenter(), obstacle.getBounds().getCenter()) <= Constant.ENTITY_SAFETY_ZONE){
                        point.x = Support.getRandomValue(Constant.BOUNDS_DISTANCE + Constant.BUILDING_STROKE + Constant.BUILDING_DISTANCE_LEFT,
                                this.getWidth() - Constant.BUILDING_DISTANCE_RIGHT - Constant.ENTITY_SIZE - 2 * Constant.BOUNDS_DISTANCE - 1);
                        point.y = Support.getRandomValue(Constant.BOUNDS_DISTANCE + Constant.BUILDING_DISTANCE_UP_DOWN + Constant.BUILDING_STROKE,
                                this.getHeight() - Constant.BUILDING_DISTANCE_UP_DOWN - Constant.BUILDING_STROKE - Constant.ENTITY_SIZE - 2 * Constant.BOUNDS_DISTANCE - 1);

                        o.setPosition(point);
                    }
                }
            }*/

            this.obstacles.add(i, o);
        }

        Support.sortObstacles(this.obstacles);
    }

    /**
     *
     * */
    private void createWayPoints(){
        this.wayPoints = new ArrayList<>();

        for(int i = 0; i < this.numberOfWayPoints; i++){

            Point point = new Point();
            point.x = Support.getRandomValue(Constant.BOUNDS_DISTANCE + Constant.BUILDING_STROKE + Constant.BUILDING_DISTANCE_LEFT,
                    this.getWidth() - Constant.BUILDING_DISTANCE_RIGHT - Constant.ENTITY_SIZE - 2 * Constant.BOUNDS_DISTANCE - 1);
            point.y = Support.getRandomValue(Constant.BOUNDS_DISTANCE + Constant.BUILDING_DISTANCE_UP_DOWN + Constant.BUILDING_STROKE,
                    this.getHeight() - Constant.BUILDING_DISTANCE_UP_DOWN - Constant.BUILDING_STROKE - Constant.ENTITY_SIZE - 2*Constant.BOUNDS_DISTANCE - 1);

            WayPoint w = new WayPoint(point);
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
            Point point = new Point();
            point.x = Support.getRandomValue(Constant.ENTITY_SIZE + Constant.BOUNDS_DISTANCE,  Constant.BUILDING_DISTANCE_LEFT - Constant.ENTITY_SIZE - 2*Constant.BOUNDS_DISTANCE);
            point.y = Support.getRandomValue(Constant.ENTITY_SIZE + Constant.BOUNDS_DISTANCE, this.getHeight() - Constant.ENTITY_SIZE - Constant.BOUNDS_DISTANCE);
            Pedestrian p = new Pedestrian(point, i);
            this.crowd.add(i, p);
        }

        //divide the crowd into groups
        int groupIndex = 0;
        this.numberOfGroups = (int)Math.floor((double) numberOfPeople/sizeOfGroups);
        for(int i = 1; i <= numberOfGroups; i++){
            List<WayPoint> goalsList = goalsList();

            Group group = new Group(i, goalsList, crowd.subList(groupIndex, groupIndex + sizeOfGroups));
            group.setColor(new Color(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat()));

            for(Pedestrian p : group.getPedestrians())
                p.setGoalsList(new ArrayList<>(goalsList));


            this.groups.add(i-1, group);
            groupIndex += sizeOfGroups;
        }
        if(numberOfPeople%sizeOfGroups != 0){
            this.numberOfGroups++;
            this.groups.add(0, new Group(0, goalsList(), crowd.subList(groupIndex, crowd.size())));
        }

        //create crowd goals list
        crowdGoals = new ArrayList<>();
        for(Group o: groups){
            for(WayPoint w: o.getGoalsList())
                if(!crowdGoals.contains(w))
                    crowdGoals.add(w);
        }
    }

    /**
     * Assign a list of goals to each group
     * */
    private List<WayPoint> goalsList(){
        ArrayList<WayPoint> list = new ArrayList<>(this.wayPoints);

        list.removeIf(w -> Support.getRandomValue(1, 10) <= 4); //40% of probability to remove a way point from the initial list

        //add Entrance as the first way point
        //list.add(0, new WayPoint(new Point(Constant.BUILDING_DISTANCE_LEFT, this.getHeight()/2 + 15)));

        return list;
    }


    /***************************************    ACCESSORS    *****************************************/
    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public int getSizeOfGroups() {
        return sizeOfGroups;
    }

    public int getNumberOfObstacles() {
        return numberOfObstacles;
    }

    public int getNumberOfWayPoints() {
        return numberOfWayPoints;
    }

    public List<Pedestrian> getCrowd() {
        return crowd;
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
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

    public void setParameters(int numberOfPeople, int numberOfGroups, int numberOfObstacles, int numberOfWayPoints){
        this.sizeOfGroups = numberOfGroups;
        this.numberOfObstacles = numberOfObstacles;
        this.numberOfPeople = numberOfPeople;
        this.numberOfWayPoints = numberOfWayPoints;
    }

    public boolean missingParameters(){
        if (this.sizeOfGroups == 0 || this.numberOfObstacles == 0 || this.numberOfPeople == 0 || this.numberOfWayPoints == 0)
            return true;
        return false;
    }

}

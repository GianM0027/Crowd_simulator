import models.Obstacle;
import models.Pedestrian;
import models.WayPoint;
import support.Support;
import support.constants.Constant;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Contain all the settings and contents of the actual simulation
 * */
public class Simulation extends JPanel{
    private static Simulation instance;
    private Animation animation;
    private boolean isRunning;

    private ArrayList<Pedestrian> crowd;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<WayPoint> wayPoints;

    private int numberOfPeople;
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
        createCrowd();
        createObstacles();
        createWayPoints();

        //Printing entities on "active entities panel"
        ActiveEntitiesPanel.getInstance().setObstaclesTab();
        ActiveEntitiesPanel.getInstance().setWayPointsTab();
        ActiveEntitiesPanel.getInstance().setPedestriansTab(this.crowd);
        ActiveEntitiesPanel.getInstance().enableFilters();
        ActiveEntitiesPanel.getInstance().updateFilteredCrowd();

        this.removeAll();
        this.revalidate();
        this.repaint();
        animation = new Animation(this, this.crowd, this.obstacles, this.wayPoints);
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


    /***************************************    ENTITIES CREATION    *****************************************/
    /**
     *
     * */
    private void createObstacles(){
        this.obstacles = new ArrayList<>();

        for(int i = 0; i < this.numberOfObstacles; i++){
            Point point = new Point();
            point.x = Support.getRandomValue(Constant.ENTITY_SIZE + Constant.BOUNDS_DISTANCE, this.getWidth() - Constant.ENTITY_SIZE + Constant.BOUNDS_DISTANCE);
            point.y = Support.getRandomValue(Constant.ENTITY_SIZE + Constant.BOUNDS_DISTANCE, this.getHeight() - Constant.ENTITY_SIZE + Constant.BOUNDS_DISTANCE);

            Obstacle o = new Obstacle(point);
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
            point.x = Support.getRandomValue(Constant.ENTITY_SIZE + Constant.BOUNDS_DISTANCE, this.getWidth() - Constant.ENTITY_SIZE + Constant.BOUNDS_DISTANCE);
            point.y = Support.getRandomValue(Constant.ENTITY_SIZE + Constant.BOUNDS_DISTANCE, this.getHeight() - Constant.ENTITY_SIZE + Constant.BOUNDS_DISTANCE);

            WayPoint w = new WayPoint(point);
            this.wayPoints.add(i, w);
        }
        Support.sortWayPoints(this.wayPoints);
    }

    /**
     *
     * */
    private void createCrowd(){
        this.crowd = new ArrayList<>();

        for(int i = 0; i < numberOfPeople; i++){
            Point point = new Point();
            point.x = Support.getRandomValue(Constant.ENTITY_SIZE + Constant.BOUNDS_DISTANCE, this.getWidth() - Constant.ENTITY_SIZE + Constant.BOUNDS_DISTANCE);
            point.y = Support.getRandomValue(Constant.ENTITY_SIZE + Constant.BOUNDS_DISTANCE, this.getHeight() - Constant.ENTITY_SIZE + Constant.BOUNDS_DISTANCE);
            Pedestrian p = new Pedestrian(point, i, null);
            this.crowd.add(i, p);
        }

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

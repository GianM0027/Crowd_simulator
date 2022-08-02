import models.Crowd;
import models.Obstacle;
import models.WayPoint;
import support.constants.Constant;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Simulation extends JPanel{

    private static Simulation instance;

    private Crowd crowd;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<WayPoint> wayPoints;

    private int numberOfPeople;
    private int numberOfGroups;
    private int numberOfObstacles;
    private int numberOfWayPoints;

    public Simulation(){
        this.numberOfGroups = 0;
        this.numberOfObstacles = 0;
        this.numberOfPeople = 0;
        this.numberOfWayPoints = 0;
    }


    //only one instance of the simulation at a time
    public static Simulation getInstance(){
        if(instance == null)
            instance = new Simulation();
        return instance;
    }


    protected void startSimulation(){
        if(!missingInputs()){
            this.crowd = new Crowd(this.numberOfPeople);
            createObstacles();
            createWayPoints();

            //Printing entities on "active entities panel"
            ActiveEntitiesPanel.getInstance().setObstaclesTab();
            ActiveEntitiesPanel.getInstance().setWayPointsTab();
            //setta anche pannello dei pedestrian
        }
    }

    protected void pauseSimulation(){
        this.setBackground(Color.GRAY);
    }
    protected void stopSimulation(){
        setParameters(0,0,0,0);
        this.setBackground(Color.WHITE);
    }

    private void createObstacles(){
        this.obstacles = new ArrayList<>();

        for(int i = 0; i < this.numberOfObstacles; i++){
            Point point = new Point();
            Obstacle o = new Obstacle(point);
            point.x = new Random().nextInt(this.getWidth());
            point.y = new Random().nextInt(this.getHeight());
            o.setPosition(point);
            this.obstacles.add(i, o);
        }
    }

    private void createWayPoints(){
        this.wayPoints = new ArrayList<WayPoint>();

        for(int i = 0; i < this.numberOfWayPoints; i++){
            Point point = new Point();
            WayPoint w = new WayPoint(point);
            point.x = new Random().nextInt(this.getWidth());
            point.y = new Random().nextInt(this.getHeight());
            w.setPosition(point);
            this.wayPoints.add(i, w);
        }
    }


    public boolean missingInputs(){
        if (this.numberOfGroups == 0 || this.numberOfPeople == 0 || this.numberOfObstacles == 0 || this.numberOfWayPoints == 0)
            return true;
        else
            return false;
    }



    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public int getNumberOfGroups() {
        return numberOfGroups;
    }

    public int getNumberOfObstacles() {
        return numberOfObstacles;
    }

    public int getNumberOfWayPoints() {
        return numberOfWayPoints;
    }

    public Crowd getCrowd() {
        return crowd;
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public ArrayList<WayPoint> getWayPoints() {
        return wayPoints;
    }

    public void setParameters(int numberOfPeople, int numberOfGroups, int numberOfObstacles, int numberOfWayPoints){
        this.numberOfGroups = numberOfGroups;
        this.numberOfObstacles = numberOfObstacles;
        this.numberOfPeople = numberOfPeople;
        this.numberOfWayPoints = numberOfWayPoints;
    }

}

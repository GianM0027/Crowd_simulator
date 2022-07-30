import javax.swing.*;
import java.awt.*;

public class Simulation extends JPanel{

    private static Simulation instance;

    private int numberOfPeople;
    private int numberOfGroups;
    private int numberOfObstacles;
    private int numberOfWayPoints;

    public Simulation(){
        this.numberOfGroups = 0;
        this.numberOfObstacles = 0;
        this.numberOfPeople = 0;
        this.numberOfWayPoints = 0;

        //set building bounds
    }


    //only one instance of the simulation at a time
    public static Simulation getInstance(){
        if(instance == null)
            instance = new Simulation();
        return instance;
    }

    public void paint(Graphics g){

        //drawing building
        Graphics2D building = (Graphics2D) g;
        building.drawLine(0,0,100,100);
    }

    protected void startSimulation(){
        if(checkInputValues()){
            System.out.println("Simulation Started");
            this.setBackground(Color.GREEN);
        }
        else {
            System.out.println("Missing Parameters");
            this.setBackground(Color.RED);
        }

    }
    protected void pauseSimulation(){
        this.setBackground(Color.GRAY);
    }
    protected void stopSimulation(){
        this.setBackground(Color.WHITE);
    }


    private boolean checkInputValues(){
        if (this.numberOfGroups == 0 || this.numberOfPeople == 0 || this.numberOfObstacles == 0 || this.numberOfWayPoints == 0)
            return false;
        else
            return true;
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

    public void setParameters(int numberOfPeople, int numberOfGroups, int numberOfObstacles, int numberOfWayPoints){
        setNumberOfGroups(numberOfGroups);
        setNumberOfObstacles(numberOfObstacles);
        setNumberOfPeople(numberOfPeople);
        setNumberOfWayPoints(numberOfWayPoints);
    }


    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public void setNumberOfGroups(int numberOfGroups) {
        this.numberOfGroups = numberOfGroups;
    }

    public void setNumberOfObstacles(int numberOfObstacles) {
        this.numberOfObstacles = numberOfObstacles;
    }

    public void setNumberOfWayPoints(int numberOfWayPoints) {
        this.numberOfWayPoints = numberOfWayPoints;
    }
}

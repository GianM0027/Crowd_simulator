import javax.swing.*;
import java.awt.*;

public class Simulation {

    private static Simulation instance;
    private JPanel simulationPanel;

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
        if(checkInputValues()){
            System.out.println("Simulation Started");
            this.simulationPanel.setBackground(Color.GREEN);
        }
        else {
            System.out.println("Missing Parameters");
            this.simulationPanel.setBackground(Color.RED);
        }

    }
    protected void pauseSimulation(){
        this.simulationPanel.setBackground(Color.GRAY);
    }
    protected void stopSimulation(){
        this.simulationPanel.setBackground(Color.WHITE);
    }


    private boolean checkInputValues(){
        if (this.numberOfGroups == 0 || this.numberOfPeople == 0 || this.numberOfObstacles == 0 || this.numberOfWayPoints == 0)
            return false;
        else
            return true;
    }


    public JPanel getSimulationPanel() {
        return simulationPanel;
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

    public void setSimulationPanel(JPanel simulationPanel) {
        this.simulationPanel = simulationPanel;
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

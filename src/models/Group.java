package models;

import processing.core.PVector;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * A group of pedestrians
 * */
public class Group{
    private boolean startWalking;
    private List<Pedestrian> pedestrians;
    private PVector avgGroupPosition;
    private List<WayPoint> goalsList;
    private int groupID;
    private Color color;
    private boolean isMoving;
    private Timer timer;


    public Group(int groupID, List<WayPoint> goalsList, List<Pedestrian> pedestrians){
        this.startWalking = false;
        this.groupID = groupID;
        this.pedestrians = pedestrians;
        this.goalsList = goalsList;
        this.isMoving = true;

        for(Pedestrian p: pedestrians) {
            p.setGoalsList(goalsList);
            p.setGroupID(groupID);
        }

        timer = new Timer(groupID*6000, e -> setStartWalking(true));
        timer.start();
    }


    public List<Pedestrian> getPedestrians() {
        return pedestrians;
    }

    public int getGroupID() {
        return groupID;
    }
    public String getGoalPointstoString(){
        String result = "{";

        for(int i = 0; i < goalsList.size(); i++) {
            result += (goalsList.get(i).getWaypointID());
            result += ", ";
        }

        return result.subSequence(0,result.length()-2) + "}";
    }

    public void removeFirstGoal(){
        goalsList.remove(0);

        for(Pedestrian p : this.pedestrians)
            p.updatePath();
    }

    public Color getColor() {
        return color;
    }

    public PVector getAvgGroupPosition() {
        return avgGroupPosition;
    }

    public void setAvgGroupPosition(PVector avgGroupPosition) {
        if(pedestrians.size() > 1)
            this.avgGroupPosition = avgGroupPosition;
        else
            this.avgGroupPosition = new PVector((float)pedestrians.get(0).getPosition().getX(), (float)pedestrians.get(0).getPosition().getY());
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public boolean hasStartedWalking() {
        return startWalking;
    }

    public void setStartWalking(boolean startWalking) {
        this.startWalking = startWalking;
        timer.stop();
    }

    public String getGroupSimbol(){
        return "\u2B1B";
    }
}

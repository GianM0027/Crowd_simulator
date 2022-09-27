package models;

import support.Support;
import support.constants.Constant;

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
    private List<WayPoint> goalsList;
    private int groupID;
    private Color color;
    private boolean isMoving;


    public Group(int groupID, List<WayPoint> goalsList, List<Pedestrian> pedestrians){
        this.startWalking = false;
        this.groupID = groupID;
        this.pedestrians = pedestrians;
        this.goalsList = goalsList;
        this.isMoving = true;

        for(Pedestrian p: pedestrians) {
            p.setGoalsList(new ArrayList<>(goalsList));
            p.setGroupID(groupID);
        }

        new Timer(groupID*5000, e -> setStartWalking(true)).start();
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
            if(goalsList.get(i).getWaypointID() != -1) {
                result += (goalsList.get(i).getWaypointID());
                result += ", ";
            }
        }

        return result.subSequence(0,result.length()-2) + "}";
    }

    public Color getColor() {
        return color;
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
    }

    public String getGroupSimbol(){
        return "\u2B1B";
    }
}

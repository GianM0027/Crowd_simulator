package models;

import com.google.gson.annotations.Expose;
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
    @Expose
    private int groupID;
    @Expose
    private List<Pedestrian> pedestrians;
    private PVector avgGroupPosition;
    @Expose
    private List<WayPoint> goalsList;
    private List<WayPoint> restingPoints;
    private Color color;
    private boolean isMoving;
    private boolean isRestingTime;
    private boolean isGoingToRest;
    private Timer startWalkingTimer;


    public Group(int groupID, List<WayPoint> goalsList, List<Pedestrian> pedestrians){
        this.startWalking = false;
        this.groupID = groupID;
        this.pedestrians = pedestrians;
        this.goalsList = goalsList;
        this.isMoving = true;
        this.isRestingTime = false;
        this.isGoingToRest = false;

        for(Pedestrian p: pedestrians) {
            p.setGoalsList(goalsList);
            p.setGroupID(groupID);
        }

        startWalkingTimer = new Timer(groupID*2000, e -> startWalkingTimers());
        startWalkingTimer.setRepeats(false);
        startWalkingTimer.start();
    }

    private void startWalkingTimers(){
        this.startWalking = true;

        for(Pedestrian p : this.pedestrians)
            p.getEnergyWasteTimer().start();
    }


    public List<Pedestrian> getPedestrians() {
        return pedestrians;
    }

    public int getGroupID() {
        return groupID;
    }
    public String getGoalPointstoString(){

        if(!goalsList.isEmpty()) {
            String result = "{";

            for (int i = 0; i < goalsList.size(); i++) {
                result += (goalsList.get(i).getWaypointID());
                result += ", ";
            }

            return result.subSequence(0, result.length() - 2) + "}";
        }
        else return "None";
    }

    public void removeFirstGoal(){
        goalsList.remove(0);
    }

    public Color getColor() {
        return color;
    }


    public boolean isRestTime(){
        return isRestingTime;
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

    public Timer getStartWalkingTimer() {
        return startWalkingTimer;
    }

    public void setStartWalkingTimer(Timer startWalkingTimer) {
        this.startWalkingTimer = startWalkingTimer;
    }

    public boolean isStartWalking() {
        return startWalking;
    }

    public List<WayPoint> getRestingPoints() {
        return restingPoints;
    }

    public void setRestingTime(boolean restingTime) {
        isRestingTime = restingTime;
    }

    public boolean isGoingToRest() {
        return isGoingToRest;
    }

    public void setGoingToRest(boolean goingToRest) {
        isGoingToRest = goingToRest;
    }

    public void setRestingPoints(List<WayPoint> rp){
        this.restingPoints = rp;
    }

    public String getGroupSimbol(){
        return "\u2B1B";
    }
}

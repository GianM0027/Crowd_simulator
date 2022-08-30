package models;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;



/**
 * A group of pedestrians
 * */
public class Group {
    private int sizeGroup;
    private List<Pedestrian> pedestrians;
    private List<WayPoint> goalsList;
    private int groupID;
    private Color color;
    private boolean isActive;


    public Group(int groupID, List<WayPoint> goalsList, List<Pedestrian> pedestrians){
        this.sizeGroup = pedestrians.size();
        this.groupID = groupID;
        this.pedestrians = pedestrians;
        this.goalsList = goalsList;
        this.isActive = false;

        for(Pedestrian p: pedestrians)
            p.setGoalsList(new ArrayList<>(goalsList));
    }



    public int getSizeGroup() {
        return sizeGroup;
    }

    public List<Pedestrian> getPedestrians() {
        return pedestrians;
    }

    public List<WayPoint> getGoalsList() {
        return goalsList;
    }

    public int getGroupID() {
        return groupID;
    }

    public Color getColor() {
        return color;
    }

    public void setGoalsList(List<WayPoint> goalsList) {
        this.goalsList = goalsList;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}

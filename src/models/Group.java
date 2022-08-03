package models;

import java.util.List;

/**
 * A group of pedestrians
 * */
public class Group {
    private int sizeGroup;
    private List<Pedestrian> pedestrians;
    private List<WayPoint> wayPoints;
    //Key Frame (pag 142 simulating crowd)

    public Group(int sizeGroup){
        this.sizeGroup = sizeGroup;
    }
}

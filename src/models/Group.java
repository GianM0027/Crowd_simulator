package models;

import java.util.List;

public class Group {
    private int sizeGroup;
    private List<Pedestrian> pedestrians;
    private List<WayPoint> wayPoints;
    private int id;
    //Key Frame (pag 142 simulating crowd)

    public Group(int sizeGroup){
        this.sizeGroup = sizeGroup;
    }
}

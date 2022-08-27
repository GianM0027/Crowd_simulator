package models;

import support.constants.Constant;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class Building {
    private Rectangle2D externalStructure;
    private double width;
    private double height;
    private Rectangle2D entranceDoor;
    private Rectangle2D exitDoor;
    public Building(int panelWidth, int panelHeight){
        this.width = (double)panelWidth - Constant.BUILDING_DISTANCE_LEFT - Constant.BUILDING_DISTANCE_RIGHT - 1;
        this.height = (double)panelHeight - Constant.BUILDING_DISTANCE_UP_DOWN - 20;


        externalStructure = new Rectangle2D.Double(Constant.BUILDING_DISTANCE_LEFT, Constant.BUILDING_DISTANCE_UP_DOWN, width, height);

        System.out.println("Panel width: " +panelWidth + "\tPanel height: " + panelHeight);
        System.out.println("Building width: " + width + "\tPanel height: " + height);
    }

    public Rectangle2D getExternalStructure() {
        return externalStructure;
    }

    public Rectangle2D getEntranceDoor() {
        return entranceDoor;
    }

    public Rectangle2D getExitDoor() {
        return exitDoor;
    }
}
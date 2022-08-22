package models;


import support.constants.Constant;

import java.awt.*;

public class Building {
    private Point entranceDoor;
    private Point exitDoor;

    private Point entranceUpUp;
    private Point entranceUpDown;
    private Point entranceDownUp;
    private Point entranceDownDown;

    private Point exitUpUp;
    private Point exitUpDown;
    private Point exitDownUp;
    private Point exitDownDown;

    public Building(){
        entranceDoor = new Point();
        exitDoor = new Point();
    }

    public void setEntranceUpUp(Point entranceUpUp) {
        this.entranceUpUp = entranceUpUp;
    }

    public void setEntranceUpDown(Point entranceUpDown) {
        this.entranceUpDown = entranceUpDown;
        this.entranceDoor.x = entranceUpDown.x;
        this.entranceDoor.y = entranceUpDown.y + Constant.BUILDING_DOOR_SIZE/2;
    }

    public void setEntranceDownUp(Point entranceDownUp) {
        this.entranceDownUp = entranceDownUp;
    }

    public void setEntranceDownDown(Point entranceDownDown) {
        this.entranceDownDown = entranceDownDown;
    }

    public void setExitUpUp(Point exitUpUp) {
        this.exitUpUp = exitUpUp;
    }

    public void setExitUpDown(Point exitUpDown) {
        this.exitUpDown = exitUpDown;
        this.exitDoor.x = exitUpDown.x;
        this.exitDoor.y = exitUpDown.y + Constant.BUILDING_DOOR_SIZE/2;
    }

    public void setExitDownUp(Point exitDownUp) {
        this.exitDownUp = exitDownUp;
    }

    public void setExitDownDown(Point exitDownDown) {
        this.exitDownDown = exitDownDown;
    }

    public Point getEntranceUpUp() {
        return entranceUpUp;
    }

    public Point getEntranceUpDown() {
        return entranceUpDown;
    }

    public Point getEntranceDownUp() {
        return entranceDownUp;
    }

    public Point getEntranceDownDown() {
        return entranceDownDown;
    }

    public Point getExitUpUp() {
        return exitUpUp;
    }

    public Point getExitUpDown() {
        return exitUpDown;
    }

    public Point getExitDownUp() {
        return exitDownUp;
    }

    public Point getExitDownDown() {
        return exitDownDown;
    }
}
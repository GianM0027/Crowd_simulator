package models;


import support.constants.Constant;

import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;

public class Building {
    private Rectangle entranceUp;
    private Rectangle entranceDown;
    private Rectangle exitUp;
    private Rectangle exitDown;
    private Rectangle wallUp;
    private Rectangle wallDown;


    public void setEntranceUp(int x, int y, int width, int height) {
        this.entranceUp = new Rectangle(x, y, width, height);
    }

    public void setEntranceDown(int x, int y, int width, int height) {
        this.entranceDown = new Rectangle(x, y, width, height);
    }

    public void setExitUp(int x, int y, int width, int height) {
        this.exitUp = new Rectangle(x, y, width, height);
    }

    public void setExitDown(int x, int y, int width, int height) {
        this.exitDown = new Rectangle(x, y, width, height);
    }

    public void setWallUp(int x, int y, int width, int height) {
        this.wallUp = new Rectangle(x, y, width, height);
    }

    public void setWallDown(int x, int y, int width, int height) {
        this.wallDown = new Rectangle(x, y, width, height);
    }


    public Rectangle getEntranceUp() {
        return entranceUp;
    }

    public Rectangle getEntranceDown() {
        return entranceDown;
    }

    public Rectangle getExitUp() {
        return exitUp;
    }

    public Rectangle getExitDown() {
        return exitDown;
    }

    public Rectangle getWallUp() {
        return wallUp;
    }

    public Rectangle getWallDown() {
        return wallDown;
    }
}

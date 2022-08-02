package models;

import support.Bounds;

import java.awt.*;

public class FixedEntity {
    private Point position;
    private Bounds bounds;

    public FixedEntity(Point position) {
        this.position = position;
        this.bounds = new Bounds(position);
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }
}

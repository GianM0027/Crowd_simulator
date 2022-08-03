package models;

import support.Bounds;

import java.awt.*;

/**
 * Every entity that does not have to move around is a "FixedEntity" (obstacles and way points)
 * */
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

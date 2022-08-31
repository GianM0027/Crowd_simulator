package models;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Every entity that does not have to move around is a "FixedEntity" (obstacles and way points)
 * */
public abstract class Entity {
    protected int entityType;
    protected Color color;
    protected Point2D position;

    public Entity(Point2D position) {
        this.position = position;
    }


    public int getEntityType() {
        return entityType;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Point2D getPosition() {
        return position;
    }

    public String getPositionString(){
        return "[" + (int)this.position.getX() + ", " + (int)this.position.getY() + "]";
    }
}

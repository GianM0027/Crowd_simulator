package models;

import support.Bounds;

import java.awt.*;

/**
 * Every entity that does not have to move around is a "FixedEntity" (obstacles and way points)
 * */
public abstract class Entity {

    protected int entityType;
    protected Color color;
    protected Point position;
    protected Bounds bounds;

    public Entity(Point position) {
        this.position = position;
        this.bounds = new Bounds(this);
    }

    public Point getPosition() {
        return position;
    }
    public Bounds getBounds() {
        return bounds;
    }

    public int getEntityType() {
        return entityType;
    }

    public Color getColor() {
        return color;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public String getPositionString(){
        return "[" + this.position.x + ", " + this.position.y + "]";
    }
}

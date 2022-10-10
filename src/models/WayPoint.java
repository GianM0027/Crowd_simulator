package models;

import processing.core.PVector;
import support.EntityBound;
import support.constants.Constant;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class WayPoint extends Entity {
    private EntityBound bounds;
    private int waypointID;
    private Rectangle2D wayPointShape;
    private Room room;
    private boolean restingArea;

    public WayPoint(Point2D position){
        super(position);
        entityType = Constant.GENERIC_WAYPOINT;

        waypointID = -1;
        wayPointShape = new Rectangle2D.Double(position.getX(), position.getY(), Constant.WAYPOINT_WIDTH, Constant.WAYPOINT_HEIGHT);
        bounds = new EntityBound(this);
    }

    /**
     * Collision with another entity
     * */
    public boolean checkCollision(Entity entity){
        EntityBound entityBounds = new EntityBound(entity);

        return entityBounds.getBoundsRectangle().intersects(this.getEntityBounds().getBoundsRectangle());
    }

    public Point2D getPosition() {
        return this.position;
    }

    public EntityBound getEntityBounds() {
        return bounds;
    }

    public Rectangle2D getShape() {
        return wayPointShape;
    }

    public int getWaypointID() {
        return waypointID;
    }

    public PVector getVectorPosition(){
        return new PVector((float)this.position.getX(), (float)this.position.getY());
    }

    public void setWaypointID(int waypointID) {
        this.waypointID = waypointID;

        //every 5 waypoints, 1 is a resting area
        if(waypointID % Constant.PERCENTAGE_OF_REST_POINTS == 0)
            this.restingArea = true;
    }

    public boolean isRestingArea() {
        return this.restingArea;
    }

    public Room getRoom() {
        return this.room;
    }
    public void setRoom(Room room){
        this.room = room;
    }
}

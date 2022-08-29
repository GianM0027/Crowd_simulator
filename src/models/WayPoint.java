package models;

import support.EntityBound;
import support.constants.Constant;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class WayPoint extends Entity {
    private EntityBound bounds;
    private Ellipse2D wayPointShape;

    public WayPoint(Point2D position){
        super(position);
        entityType = Constant.WAY_POINT;

        wayPointShape = new Ellipse2D.Double(position.getX(), position.getY(), Constant.WAYPOINT_SIZE, Constant.WAYPOINT_SIZE);
        bounds = new EntityBound(this);
    }

    public Point2D getPosition() {
        return this.position;
    }

    public EntityBound getBounds() {
        return bounds;
    }

    public Ellipse2D getWayPointShape() {
        return wayPointShape;
    }
}

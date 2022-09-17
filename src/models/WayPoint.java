package models;

import support.EntityBound;
import support.constants.Constant;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class WayPoint extends Entity {
    private EntityBound bounds;
    private Ellipse2D wayPointShape;

    public WayPoint(Point2D position){
        super(position);
        entityType = Constant.GENERIC_WAYPOINT;

        wayPointShape = new Ellipse2D.Double(position.getX(), position.getY(), Constant.WAYPOINT_WIDTH, Constant.WAYPOINT_HEIGHT);
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

    public Ellipse2D getWayPointShape() {
        return wayPointShape;
    }
}

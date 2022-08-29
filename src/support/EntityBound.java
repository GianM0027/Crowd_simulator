package support;


import models.Entity;
import support.constants.Constant;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Calculates bounds of any entity
 * */
public class EntityBound{
    private Rectangle2D boundsRectangle;

    private Point2D center; //exact position of the entity
    private Point2D upLeft;
    private Point2D upRight;
    private Point2D bottomLeft;
    private Point2D bottomRight;
    private Point2D up;
    private Point2D left;
    private Point2D right;
    private Point2D bottom;

    private double width; //width of the shape created from bounds
    private double height; //width of the shape created from bounds

    /**
     * Initialize bounds with respect to BOUNDS_DISTANCE constant
     * */
    public EntityBound(Entity entity){

        Point2D position = new Point2D.Double(entity.getPosition().getX(), entity.getPosition().getY());

        //if the entity is a pedestrian use the "Constant.PEDESTRIAN_SIZE" to compute bounds
        if(entity.getEntityType() == Constant.PEDESTRIAN) {
            this.center = new Point2D.Double(position.getX() + Constant.PEDESTRIAN_SIZE / 2d, position.getY() + Constant.PEDESTRIAN_SIZE / 2d);

            this.up = new Point2D.Double(this.center.getX(), position.getY() - Constant.BOUNDS_DISTANCE);
            this.bottom = new Point2D.Double(this.center.getX(), position.getY() + Constant.PEDESTRIAN_SIZE + Constant.BOUNDS_DISTANCE);

            this.left = new Point2D.Double(position.getX() - Constant.BOUNDS_DISTANCE, this.center.getY());
            this.right = new Point2D.Double(position.getX() + Constant.PEDESTRIAN_SIZE + Constant.BOUNDS_DISTANCE, this.center.getY());
        }

        //if the entity is a Obstacle use the "Constant.OBSTACLE_SIZE" to compute bounds
        else if(entity.getEntityType() == Constant.OBSTACLE) {
            this.center = new Point2D.Double(position.getX() + Constant.OBSTACLE_SIZE / 2d, position.getY() + Constant.OBSTACLE_SIZE / 2d);

            this.up = new Point2D.Double(this.center.getX(), position.getY() - Constant.BOUNDS_DISTANCE);
            this.bottom = new Point2D.Double(this.center.getX(), position.getY() + Constant.OBSTACLE_SIZE + Constant.BOUNDS_DISTANCE);

            this.left = new Point2D.Double(position.getX() - Constant.BOUNDS_DISTANCE, this.center.getY());
            this.right = new Point2D.Double(position.getX() + Constant.OBSTACLE_SIZE + Constant.BOUNDS_DISTANCE, this.center.getY());
        }

        //if the entity is a WayPoint use the "Constant.WAYPOINT_SIZE" to compute bounds
        else {
            this.center = new Point2D.Double(position.getX() + Constant.WAYPOINT_SIZE / 2d, position.getY() + Constant.WAYPOINT_SIZE / 2d);

            this.up = new Point2D.Double(this.center.getX(), position.getY() - Constant.BOUNDS_DISTANCE);
            this.bottom = new Point2D.Double(this.center.getX(), position.getY() + Constant.WAYPOINT_SIZE + Constant.BOUNDS_DISTANCE);

            this.left = new Point2D.Double(position.getX() - Constant.BOUNDS_DISTANCE, this.center.getY());
            this.right = new Point2D.Double(position.getX() + Constant.WAYPOINT_SIZE + Constant.BOUNDS_DISTANCE, this.center.getY());
        }

        this.upLeft = new Point2D.Double(this.left.getX(), this.up.getY());
        this.upRight = new Point2D.Double(this.right.getX(), this.up.getY());
        this.bottomLeft = new Point2D.Double(this.left.getX(), this.bottom.getY());
        this.bottomRight = new Point2D.Double(this.right.getX(), this.bottom.getY());

        this.width = this.right.getX() - this.left.getX();
        this.height = this.up.getY() - this.bottom.getY();

        this.boundsRectangle = new Rectangle2D.Double(upLeft.getY(), upLeft.getY(), width, height);
    }

    public Rectangle2D getBoundsRectangle() {
        return boundsRectangle;
    }

    public Point2D getCenter() {
        return center;
    }

    public Point2D getUpLeft() {
        return upLeft;
    }

    public Point2D getUpRight() {
        return upRight;
    }

    public Point2D getBottomLeft() {
        return bottomLeft;
    }

    public Point2D getBottomRight() {
        return bottomRight;
    }

    public Point2D getUp() {
        return up;
    }

    public Point2D getLeft() {
        return left;
    }

    public Point2D getRight() {
        return right;
    }

    public Point2D getBottom() {
        return bottom;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
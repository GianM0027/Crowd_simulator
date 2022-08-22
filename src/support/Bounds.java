package support;


import models.Entity;
import support.constants.Constant;

import java.awt.*;

/**
 * Calculates bounds of any entity
 * */
public class Bounds {
    private Rectangle boundsRectangle;

    private Point center; //exact position of the entity
    private Point upLeft;
    private Point upRight;
    private Point bottomLeft;
    private Point bottomRight;
    private Point up;
    private Point left;
    private Point right;
    private Point bottom;

    private int width; //width of the shape created from bounds
    private int height; //width of the shape created from bounds

    /**
     * Initialize bounds with respect to BOUNDS_DISTANCE constant
     * */
    public Bounds(Entity entity){

        Point position = entity.getPosition();

        //if the entity is a pedestrian use the "Constant.PEDESTRIAN_SIZE" to compute bounds
        if(entity.getEntityType() == Constant.PEDESTRIAN) {
            this.center = new Point(position.x + Constant.PEDESTRIAN_SIZE / 2, position.y + Constant.PEDESTRIAN_SIZE / 2);

            this.up = new Point(this.center.x, position.y - Constant.BOUNDS_DISTANCE);
            this.bottom = new Point(this.center.x, position.y + Constant.PEDESTRIAN_SIZE + Constant.BOUNDS_DISTANCE);

            this.left = new Point(position.x - Constant.BOUNDS_DISTANCE, this.center.y);
            this.right = new Point(position.x + Constant.PEDESTRIAN_SIZE + Constant.BOUNDS_DISTANCE, this.center.y);
        }

        //if the entity is a Obstacle use the "Constant.OBSTACLE_SIZE" to compute bounds
        else if(entity.getEntityType() == Constant.OBSTACLE) {
            this.center = new Point(position.x + Constant.OBSTACLE_SIZE / 2, position.y + Constant.OBSTACLE_SIZE / 2);

            this.up = new Point(this.center.x, position.y - Constant.BOUNDS_DISTANCE);
            this.bottom = new Point(this.center.x, position.y + Constant.OBSTACLE_SIZE + Constant.BOUNDS_DISTANCE);

            this.left = new Point(position.x - Constant.BOUNDS_DISTANCE, this.center.y);
            this.right = new Point(position.x + Constant.OBSTACLE_SIZE + Constant.BOUNDS_DISTANCE, this.center.y);
        }

        //if the entity is a WayPoint use the "Constant.WAYPOINT_SIZE" to compute bounds
        else {
            this.center = new Point(position.x + Constant.WAYPOINT_SIZE / 2, position.y + Constant.WAYPOINT_SIZE / 2);

            this.up = new Point(this.center.x, position.y - Constant.BOUNDS_DISTANCE);
            this.bottom = new Point(this.center.x, position.y + Constant.WAYPOINT_SIZE + Constant.BOUNDS_DISTANCE);

            this.left = new Point(position.x - Constant.BOUNDS_DISTANCE, this.center.y);
            this.right = new Point(position.x + Constant.WAYPOINT_SIZE + Constant.BOUNDS_DISTANCE, this.center.y);
        }

        this.upLeft = new Point(this.left.x, this.up.y);
        this.upRight = new Point(this.right.x, this.up.y);
        this.bottomLeft = new Point(this.left.x, this.bottom.y);
        this.bottomRight = new Point(this.right.x, this.bottom.y);

        this.width = this.right.x - this.left.x;
        this.height = this.up.y - this.bottom.y;

        this.boundsRectangle = new Rectangle(upLeft.x, upLeft.y, width, height);
    }

    public Point getCenter() {
        return center;
    }

    public Point getUpLeft() {
        return upLeft;
    }

    public Point getUpRight() {
        return upRight;
    }

    public Point getBottomLeft() {
        return bottomLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    public Point getUp() {
        return up;
    }

    public Point getLeft() {
        return left;
    }

    public Point getRight() {
        return right;
    }

    public Point getBottom() {
        return bottom;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle getBoundsRectangle() {
        return boundsRectangle;
    }
}

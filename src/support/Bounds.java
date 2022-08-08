package support;


import support.constants.Constant;

import java.awt.*;

/**
 * Calculates bounds of any entity
 * */
public class Bounds {
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
    public Bounds(Point position){

        this.upLeft = new Point(position.x - Constant.BOUNDS_DISTANCE,position.y + Constant.BOUNDS_DISTANCE);
        this.upRight = new Point(position.x + Constant.BOUNDS_DISTANCE + Constant.ENTITY_SIZE,position.y + Constant.BOUNDS_DISTANCE);
        this.bottomLeft = new Point(position.x - Constant.BOUNDS_DISTANCE,position.y - Constant.ENTITY_SIZE - Constant.BOUNDS_DISTANCE);
        this.bottomRight = new Point(position.x + Constant.ENTITY_SIZE + Constant.BOUNDS_DISTANCE,position.y - Constant.ENTITY_SIZE - Constant.BOUNDS_DISTANCE);

        this.up = new Point(position.x + Constant.ENTITY_SIZE/2,position.y + Constant.BOUNDS_DISTANCE);
        this.left = new Point(position.x - Constant.BOUNDS_DISTANCE, position.y - Constant.ENTITY_SIZE/2);
        this.right = new Point(position.x + Constant.ENTITY_SIZE + Constant.BOUNDS_DISTANCE, position.y - Constant.ENTITY_SIZE/2);
        this.bottom = new Point(position.x + Constant.ENTITY_SIZE/2,position.y - Constant.ENTITY_SIZE - Constant.BOUNDS_DISTANCE);

        this.width = this.right.x - this.left.x;
        this.height = this.up.y - this.bottom.y;
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
}

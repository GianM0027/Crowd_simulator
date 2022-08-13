package support;


import support.constants.Constant;

import java.awt.*;

/**
 * Calculates bounds of any entity
 * */
public class Bounds {

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
    public Bounds(Point position){

        this.center = new Point(position.x + Constant.ENTITY_SIZE/2, position.y + Constant.ENTITY_SIZE/2);

        this.up = new Point(this.center.x, position.y - Constant.BOUNDS_DISTANCE);
        this.bottom = new Point(this.center.x,position.y + Constant.ENTITY_SIZE + Constant.BOUNDS_DISTANCE);

        this.left = new Point(position.x - Constant.BOUNDS_DISTANCE, this.center.y);
        this.right = new Point(position.x + Constant.ENTITY_SIZE + Constant.BOUNDS_DISTANCE, this.center.y);

        this.upLeft = new Point(this.left.x,this.up.y);
        this.upRight = new Point(this.right.x, this.up.y);
        this.bottomLeft = new Point(this.left.x, this.bottom.y);
        this.bottomRight = new Point(this.right.x, this.bottom.y);

        this.width = this.right.x - this.left.x;
        this.height = this.up.y - this.bottom.y;
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
}

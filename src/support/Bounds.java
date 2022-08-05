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

    /**
     * Initialize bounds with respect to BOUNDS_DISTANCE constant
     * */
    public Bounds(Point position){
        this.upLeft = new Point(position.x - Constant.BOUNDS_DISTANCE,position.y + Constant.BOUNDS_DISTANCE);
        this.upRight = new Point(position.x + Constant.BOUNDS_DISTANCE,position.y + Constant.BOUNDS_DISTANCE);
        this.bottomLeft = new Point(position.x - Constant.BOUNDS_DISTANCE,position.y - Constant.BOUNDS_DISTANCE);
        this.bottomRight = new Point(position.x - Constant.BOUNDS_DISTANCE,position.y - Constant.BOUNDS_DISTANCE);
        this.up = new Point(position.x,position.y - Constant.BOUNDS_DISTANCE);
        this.left = new Point(position.x - Constant.BOUNDS_DISTANCE, position.y);
        this.right = new Point(position.x + Constant.BOUNDS_DISTANCE, position.y);
        this.bottom = new Point(position.x,position.y - Constant.BOUNDS_DISTANCE);
    }
}

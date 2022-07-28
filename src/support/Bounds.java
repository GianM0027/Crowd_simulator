package support;


import java.awt.*;

public class Bounds {
    private Point upLeft;
    private Point upRight;
    private Point bottomLeft;
    private Point bottomRight;
    private Point up;
    private Point left;
    private Point right;
    private Point bottom;

    public Bounds(Point position){
        this.upLeft = new Point(position.x-1,position.y+1);
        this.upRight = new Point(position.x+1,position.y+1);
        this.bottomLeft = new Point(position.x-1,position.y-1);
        this.up = new Point(position.x,position.y-1);
        this.left = new Point(position.x-1,position.y);
        this.right = new Point(position.x+1,position.y);
        this.bottom = new Point(position.x,position.y-1);
    }
}

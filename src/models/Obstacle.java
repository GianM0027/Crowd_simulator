package models;

import support.constants.Constant;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Obstacle extends Entity {
    private Rectangle2D bounds;
    private Ellipse2D obstacleShape;

    public Obstacle(Point2D position){
        super(position);
        entityType = Constant.OBSTACLE;

        obstacleShape = new Ellipse2D.Double(position.getX(), position.getY(), Constant.OBSTACLE_SIZE, Constant.OBSTACLE_SIZE);
        bounds = obstacleShape.getBounds2D();
    }

    public Point2D getPosition(){
        return this.position;
    }

    public Rectangle2D getBounds() {
        return bounds;
    }

    public Ellipse2D getObstacleShape() {
        return obstacleShape;
    }
}

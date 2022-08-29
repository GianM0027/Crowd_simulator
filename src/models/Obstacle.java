package models;

import support.EntityBound;
import support.Support;
import support.constants.Constant;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Obstacle extends Entity {
    private EntityBound bounds;
    private Shape obstacleShape;

    public Obstacle(Point2D position){
        super(position);
        entityType = Constant.OBSTACLE;

        if(Support.getRandomValue(1,10) <= 5)
            obstacleShape = new Ellipse2D.Double(position.getX(), position.getY(), Constant.OBSTACLE_SIZE, Constant.OBSTACLE_SIZE);
        else
            obstacleShape = new Rectangle2D.Double(position.getX(), position.getY(), Constant.OBSTACLE_SIZE, Constant.OBSTACLE_SIZE);

        bounds = new EntityBound(this);
    }

    public Point2D getPosition(){
        return this.position;
    }

    public EntityBound getBounds() {
        return bounds;
    }

    public Shape getObstacleShape() {
        return obstacleShape;
    }
}

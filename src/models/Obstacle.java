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
            obstacleShape = new Ellipse2D.Double(position.getX(), position.getY(), Constant.OBSTACLE_WIDTH, Constant.OBSTACLE_HEIGHT);
        else
            obstacleShape = new Rectangle2D.Double(position.getX(), position.getY(), Constant.OBSTACLE_WIDTH, Constant.OBSTACLE_HEIGHT);

        bounds = new EntityBound(this);
    }

    /**
     * Collision with another entity
     * */
    public boolean checkCollision(Entity entity){
        EntityBound entityBounds = new EntityBound(entity);

        return entityBounds.getBoundsRectangle().intersects(this.getEntityBounds().getBoundsRectangle());
    }

    public Point2D getPosition(){
        return this.position;
    }

    public EntityBound getEntityBounds() {
        return bounds;
    }

    public Shape getObstacleShape() {
        return obstacleShape;
    }
}

package models;

import com.google.gson.annotations.Expose;
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


    //parameters for data collection:
    @Expose
    private float central_position_x;
    @Expose
    private float central_position_y;
    @Expose
    private float radius;

    public Obstacle(Point2D position){
        super(position);
        entityType = Constant.OBSTACLE;

        if(Support.getRandomValue(1,10) <= 5)
            obstacleShape = new Ellipse2D.Double(position.getX(), position.getY(), Constant.OBSTACLE_WIDTH, Constant.OBSTACLE_HEIGHT);
        else
            obstacleShape = new Rectangle2D.Double(position.getX(), position.getY(), Constant.OBSTACLE_WIDTH, Constant.OBSTACLE_HEIGHT);

        bounds = new EntityBound(this);

        central_position_x = (float)bounds.getCenter().getX();
        central_position_y = (float)bounds.getCenter().getY();
        radius = Constant.OBSTACLE_HEIGHT/2f;
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

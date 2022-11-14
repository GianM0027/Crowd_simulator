package models;

import support.constants.Constant;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Door extends WayPoint{

    private Line2D doorLine;
    private Rectangle2D doorShape;

    private Room room;

    public Door(Point2D startPosition, Point2D endPosition, Room room) {
        super(startPosition);

        this.room = room;

        entityType = Constant.DOOR;
        doorLine = new Line2D.Double(startPosition, endPosition);

        if(startPosition.getX() != endPosition.getX())
            doorShape = new Rectangle2D.Double(startPosition.getX(), startPosition.getY(), Constant.BUILDING_DOOR_SIZE, Constant.BUILDING_STROKE);
        else
            doorShape = new Rectangle2D.Double(startPosition.getX(), startPosition.getY(), Constant.BUILDING_STROKE, Constant.BUILDING_DOOR_SIZE + 10);

        position.setLocation(doorsMiddlePoint());
    }

    public Door(Point2D position, Room room) {
        super(position);
        entityType = Constant.DOOR;

        this.room = room;
        doorShape = new Rectangle2D.Double(position.getX() - Constant.BUILDING_DOOR_SIZE/2f, position.getY(), Constant.BUILDING_DOOR_SIZE, Constant.BUILDING_STROKE);
        position.setLocation(doorsMiddlePoint());
    }

    public Rectangle2D getShape() {
        return doorShape;
    }

    public Point2D doorsMiddlePoint(){
        return new Point2D.Double(doorShape.getCenterX(), doorShape.getCenterY());
    }


}

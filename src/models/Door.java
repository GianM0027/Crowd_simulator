package models;

import support.constants.Constant;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Door extends WayPoint{

    Line2D doorLine;
    Rectangle2D doorShape;

    public Door(Point2D startPosition, Point2D endPosition) {
        super(startPosition);

        entityType = Constant.DOOR;
        doorLine = new Line2D.Double(startPosition, endPosition);
        doorShape = doorLine.getBounds2D();
    }

    public Rectangle2D getDoorShape() {
        return doorShape;
    }

    public Point2D middlePoint(){
        return new Point2D.Double(doorShape.getCenterX(), doorShape.getCenterY());
    }
}

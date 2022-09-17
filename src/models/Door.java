package models;

import support.constants.Constant;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Door extends WayPoint{

    Line2D doorShape;

    public Door(Point2D startPosition, Point2D endPosition) {
        super(startPosition);

        entityType = Constant.DOOR;
        doorShape = new Line2D.Double(startPosition, endPosition);
    }
}

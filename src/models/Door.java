package models;

import support.constants.Constant;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Door extends WayPoint{

    Line2D shape;

    public Door(Point2D position) {
        super(position);

        entityType = Constant.DOOR;
    }
}

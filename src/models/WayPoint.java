package models;

import support.constants.Constant;

import java.awt.*;

public class WayPoint extends Entity {

    public WayPoint(Point position){
        super(position);
        entityType = Constant.WAY_POINT;
    }
}

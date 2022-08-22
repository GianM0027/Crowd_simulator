package models;

import support.constants.Constant;

import java.awt.*;

public class Obstacle extends Entity {

    public Obstacle(Point position){
        super(position);
        entityType = Constant.OBSTACLE;
    }
}

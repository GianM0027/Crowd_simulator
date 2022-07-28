package models;

import support.Bounds;
import support.constants.Constant;
import java.awt.*;
import java.util.List;
import java.util.Random;


public class Pedestrian{
    private int gender;
    private int age;
    private int velocity;
    private int energy;
    private Point position;
    private Bounds bounds;
    private int group;
    private List<WayPoint> goalsList;

    public Pedestrian(Point position, int groupId, List<WayPoint> goalsList){
        this.gender = new Random().nextInt(Constant.FEMALE+1)+Constant.MALE; //random among MALE and FEMALE
        this.age = new Random().nextInt(Constant.OLD+1)+Constant.CHILD; //random among CHILD, YOUNG and OLD
        this.velocity = new Random().nextInt(Constant.MAX_VELOCITY+1)+Constant.MIN_VELOCITY;
        this.energy = assignEnergy();
        this.position = position;
        this.bounds = new Bounds(this.position);
        this.group = groupId;
        this.goalsList = goalsList;
    }

    private int assignEnergy() {
        return switch (this.age) {
            case Constant.CHILD -> new Random().nextInt(Constant.MAX_ENERGY_CHILD + 1) + Constant.MIN_ENERGY_CHILD;
            case Constant.YOUNG -> new Random().nextInt(Constant.MAX_ENERGY_YOUNG + 1) + Constant.MIN_ENERGY_YOUNG;
            case Constant.OLD -> new Random().nextInt(Constant.MAX_ENERGY_OLD + 1) + Constant.MIN_ENERGY_OLD;
            default -> 0;
        };
    }
}

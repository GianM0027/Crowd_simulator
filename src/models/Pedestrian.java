package models;

import support.Bounds;
import support.constants.Constant;
import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * Every Pedestrian is a mobile entity with its own characteristics
 * */
public class Pedestrian {
    private int gender;
    private int age;
    private int velocity;
    private int energy;
    private Point position;
    private Bounds bounds;

    public Pedestrian(Point position, int groupId, List<WayPoint> goalsList){
        this.gender = new Random().nextInt(Constant.FEMALE+1)+Constant.MALE; //random among MALE and FEMALE
        this.age = new Random().nextInt(Constant.OLD+1)+Constant.CHILD; //random among CHILD, YOUNG and OLD
        this.velocity = new Random().nextInt(Constant.MAX_VELOCITY+1)+Constant.MIN_VELOCITY;
        this.energy = assignEnergy();
        this.position = position;
        this.bounds = new Bounds(this.position);
    }

    /**
     * Assigns a value of energy to a pedestrian according to their age (the function uses constants MIN_ENERGY_* and
     * MAX_ENERGY_*)
     * */
    private int assignEnergy() {
        return switch (this.age) {
            case Constant.CHILD -> new Random().nextInt(Constant.MAX_ENERGY_CHILD + 1) + Constant.MIN_ENERGY_CHILD;
            case Constant.YOUNG -> new Random().nextInt(Constant.MAX_ENERGY_YOUNG + 1) + Constant.MIN_ENERGY_YOUNG;
            case Constant.OLD -> new Random().nextInt(Constant.MAX_ENERGY_OLD + 1) + Constant.MIN_ENERGY_OLD;
            default -> 0;
        };
    }

    public int getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getGenderString() {
        if(this.getGender() == Constant.MALE)
            return "Male";
        else
            return "Female";
    }

    public String getAgeString() {
        if(this.getAge() == Constant.CHILD)
            return "Child";
        else if(this.getAge() == Constant.YOUNG)
            return "Young";
        else
            return "Old";
    }

    public int getVelocity() {
        return velocity;
    }

    public int getEnergy() {
        return energy;
    }

    public Point getPosition() {
        return position;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }
}

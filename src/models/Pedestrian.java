package models;

import support.Bounds;
import support.Support;
import support.constants.Constant;
import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * Every Pedestrian is a mobile entity with its own characteristics
 * */
public class Pedestrian {
    private Color color;
    private int gender;
    private int age;
    private int velocity;
    private int energy;
    private Point position;
    private Bounds bounds;
    private int groupID;
    private List<WayPoint> goalsList;

    public Pedestrian(Point position, int groupId, List<WayPoint> goalsList){
        this.position = position;
        this.groupID = groupId;
        this.goalsList = goalsList;

        this.gender = Support.getRandomValue(Constant.MALE, Constant.FEMALE); //random among MALE and FEMALE
        this.age = Support.getRandomValue(Constant.CHILD, Constant.OLD); //random among CHILD, YOUNG and OLD
        this.velocity = Support.getRandomValue(Constant.MIN_VELOCITY, Constant.MAX_VELOCITY);
        this.energy = assignEnergy();
        this.bounds = new Bounds(this.position);
    }

    /**
     * Assigns a value of energy to a pedestrian according to their age (the function uses constants MIN_ENERGY_* and
     * MAX_ENERGY_*)
     * */
    private int assignEnergy() {
        return switch (this.age) {
            case Constant.CHILD -> Support.getRandomValue(Constant.MIN_ENERGY_CHILD, Constant.MAX_ENERGY_CHILD);
            case Constant.YOUNG -> Support.getRandomValue(Constant.MIN_ENERGY_YOUNG, Constant.MAX_ENERGY_YOUNG);
            case Constant.OLD -> Support.getRandomValue(Constant.MIN_ENERGY_OLD, Constant.MAX_ENERGY_OLD);
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

    public int getGroupID() {
        return groupID;
    }

    public Color getColor() {
        return color;
    }

    public List<WayPoint> getGoalsList() {
        return goalsList;
    }

    public String getPositionString(){
        return "[" + this.position.x + ", " + this.position.y + "]";
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
        this.bounds = new Bounds(position);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setGoalsList(List<WayPoint> goalsList) {
        this.goalsList = goalsList;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }
}

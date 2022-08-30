package models;

import support.EntityBound;
import support.Support;
import support.constants.Constant;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Every Pedestrian is a mobile entity with its own characteristics
 * */
public class Pedestrian extends Entity{
    private Rectangle2D pedestrianShape;
    private int gender;
    private int age;
    private int velocity;
    private int energy;
    private int groupID;
    private List<WayPoint> goalsList;
    private int goalsNumber;

    /** motion parameters */
    private double rotation;
    private EntityBound bounds;
    //position is in the superclass

    public Pedestrian(Point2D position, int groupId){
        super(position);
        entityType = Constant.PEDESTRIAN;
        pedestrianShape = new Rectangle2D.Double(position.getX(), position.getY(), Constant.PEDESTRIAN_WIDTH, Constant.PEDESTRIAN_HEIGHT);
        bounds = new EntityBound(this);

        this.groupID = groupId;
        this.goalsNumber = 0;

        this.gender = Support.getRandomValue(Constant.MALE, Constant.FEMALE); //random among MALE and FEMALE
        this.age = Support.getRandomValue(Constant.CHILD, Constant.OLD); //random among CHILD, YOUNG and OLD
        this.velocity = Support.getRandomValue(Constant.MIN_VELOCITY, Constant.MAX_VELOCITY);
        this.energy = assignEnergy();
    }

    /**
     * Collision with another entity
     * */
    public boolean checkCollision(Entity entity){
        EntityBound entityBounds = new EntityBound(entity);

        return entityBounds.getBoundsRectangle().intersects(this.getEntityBounds().getBoundsRectangle());
    }

    /**
     * Compute next position towards a goal, it ignores obstacles and other pedestrians
     * */
    public Point2D nextPosition(JPanel panel){
        Point2D goalPosition;
        Point2D nextPos = this.position;

        if(goalsList != null && !goalsList.isEmpty())
            goalPosition = goalsList.get(0).getPosition();
        else
            goalPosition = new Point2D.Double(panel.getWidth() + 10, panel.getHeight()/2d);


        //motion
        double deltaX = this.position.getX() - goalPosition.getX();
        double deltaY = this.position.getY() - goalPosition.getY();
        double angle = Math.atan2(deltaY, deltaX) + Math.PI;

        deltaY = Math.sin(angle);
        deltaX = Math.cos(angle);

        nextPos = new Point2D.Double(nextPos.getX() + deltaX, nextPos.getY() + deltaY);


        if(Support.distance(this.position, goalPosition) < this.bounds.getWidth() && !goalsList.isEmpty()){
            this.goalsList.remove(0);
        }

        if(this.position.getX() > panel.getWidth()){
            this.setGoalsList(new ArrayList<>());
        }

        return nextPos;
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

    public EntityBound getEntityBounds() {
        return bounds;
    }

    public String getAgeString() {
        if(this.getAge() == Constant.CHILD)
            return "Child";
        else if(this.getAge() == Constant.YOUNG)
            return "Young";
        else
            return "Old";
    }

    public int getGoalsNumber() {
        return goalsNumber;
    }


    public Rectangle2D getPedestrianShape() {
        return pedestrianShape;
    }

    public Point2D getPosition(){
        return this.position;
    }

    public int getVelocity() {
        return velocity;
    }

    public int getEnergy() {
        return energy;
    }

    public void setGoalsList(List<WayPoint> goalsList) {
        this.goalsList = goalsList;
        goalsNumber = goalsList.size();
    }

    public void setPosition(Point2D position){
        this.position.setLocation(position);
        this.pedestrianShape = new Rectangle2D.Double(position.getX(), position.getY(), Constant.PEDESTRIAN_WIDTH, Constant.PEDESTRIAN_HEIGHT);
        this.bounds = new EntityBound(this);
    }

}

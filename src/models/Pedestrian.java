package models;

import support.EntityBound;
import support.Support;
import support.constants.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Every Pedestrian is a mobile entity with its own characteristics
 * */
public class Pedestrian extends Entity{
    private EntityBound bounds;
    private Ellipse2D pedestrianShape;
    private int gender;
    private int age;
    private int velocity;
    private int energy;
    private int groupID;
    private List<WayPoint> goalsList;
    private int goalsNumber;

    public Pedestrian(Point2D position, int groupId){
        super(position);
        entityType = Constant.PEDESTRIAN;
        pedestrianShape = new Ellipse2D.Double(position.getX(), position.getY(), Constant.PEDESTRIAN_SIZE, Constant.PEDESTRIAN_SIZE);
        bounds = new EntityBound(this);

        this.groupID = groupId;
        this.goalsNumber = 0;

        this.gender = Support.getRandomValue(Constant.MALE, Constant.FEMALE); //random among MALE and FEMALE
        this.age = Support.getRandomValue(Constant.CHILD, Constant.OLD); //random among CHILD, YOUNG and OLD
        this.velocity = Support.getRandomValue(Constant.MIN_VELOCITY, Constant.MAX_VELOCITY);
        this.energy = assignEnergy();
    }

    /**
     * Compute next position and direction
     * */
    public Point2D nextPosition(JPanel panel){
        Point2D goalPosition;
        Point2D nextPos = this.position;

        if(goalsList != null && !goalsList.isEmpty())
            goalPosition = goalsList.get(0).getPosition();
        else
            goalPosition = new Point(panel.getWidth() + 10, panel.getHeight()/2);


        //motion
        double deltaX = this.position.getX() - goalPosition.getX();
        double deltaY = this.position.getX() - goalPosition.getX();
        double angle = Math.atan2(deltaY, deltaX) + Math.PI;

        deltaY = Math.sin(angle)/100;
        deltaX = Math.cos(angle)/100;

        nextPos = new Point2D.Double(nextPos.getX() + deltaX + 1, nextPos.getY() + deltaY + 1); //+1 non ci sta a fare nulla

        if(Support.distance(this.position, goalPosition) < this.bounds.getWidth() && !goalsList.isEmpty()){
            this.goalsList.remove(0);
        }

        if(this.position.getX() > panel.getWidth()){
            this.setGoalsList(new ArrayList<>());
        }

        return nextPos;
    }

    public Point2D obstacleAvoidance(List<Obstacle> obstacles, Point2D newPosition){
        Pedestrian p = new Pedestrian(newPosition, -1);
        Rectangle2D futureEntityBounds = p.getPedestrianShape().getBounds2D();

        for(int i = 0; i < obstacles.size(); i++){
            if(futureEntityBounds.intersects(obstacles.get(i).getObstacleShape().getBounds())){
                double deltaX = newPosition.getX() - obstacles.get(i).getPosition().getX();
                double deltaY = newPosition.getY() - obstacles.get(i).getPosition().getY();
                double angle = Math.atan2(deltaY, deltaX) + Math.toRadians(180);

                if(Support.getRandomValue(1,10) <= 5)
                    angle += 60;
                else
                    angle -= 60;

                deltaY = Math.sin(angle) * 100;
                deltaX = Math.cos(angle) * 100;

                newPosition = new Point2D.Double(newPosition.getX() + deltaX / (Constant.ANIMATION_DELAY), newPosition.getY() + deltaY / (Constant.ANIMATION_DELAY));
            }
        }

        return new Point2D.Double(newPosition.getX(), newPosition.getY());
    }

    /**
     * Collision avoidance
     * */
    public Point2D pedestrianAvoidance(List<Pedestrian> crowd, Point2D newPosition){
        int nPedestrians = crowd.size();

        for(int i = 0; i < nPedestrians; i++){
            if(Support.distance(newPosition, crowd.get(i).getPosition()) <= crowd.get(i).getPedestrianShape().getWidth()){
                double deltaX = newPosition.getX() - crowd.get(i).getPosition().getX();
                double deltaY = newPosition.getY() - crowd.get(i).getPosition().getY();
                double angle = Math.atan2(deltaY, deltaX) + Math.toRadians(180);

                if(Support.getRandomValue(1,10) <= 5)
                    angle += 60;
                else
                    angle -= 60;

                deltaY = Math.sin(angle) * 100;
                deltaX = Math.cos(angle) * 100;

                newPosition = new Point2D.Double(newPosition.getX() + deltaX / (Constant.ANIMATION_DELAY), newPosition.getY() + deltaY / (Constant.ANIMATION_DELAY));

            }
        }

        return new Point2D.Double(newPosition.getX(), newPosition.getY());
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

    public EntityBound getBounds() {
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


    public Ellipse2D getPedestrianShape() {
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
        this.pedestrianShape = new Ellipse2D.Double(position.getX(), position.getY(), Constant.PEDESTRIAN_SIZE, Constant.PEDESTRIAN_SIZE);
        this.bounds = new EntityBound(this);
    }

}

package models;

import support.Bounds;
import support.Support;
import support.constants.Constant;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
    private int goalsNumber;

    public Pedestrian(Point position, int groupId){
        this.position = position;
        this.groupID = groupId;
        this.goalsNumber = 0;

        this.gender = Support.getRandomValue(Constant.MALE, Constant.FEMALE); //random among MALE and FEMALE
        this.age = Support.getRandomValue(Constant.CHILD, Constant.OLD); //random among CHILD, YOUNG and OLD
        this.velocity = Support.getRandomValue(Constant.MIN_VELOCITY, Constant.MAX_VELOCITY);
        this.energy = assignEnergy();
        this.bounds = new Bounds(this.position);
    }

    /**
     * Compute next position and direction
     * */
    public Point nextPosition(JPanel panel){
        Point goalPosition;
        Point nextPos = this.position;

        if(goalsList != null && !goalsList.isEmpty())
            goalPosition = goalsList.get(0).getPosition();
        else
            goalPosition = new Point(panel.getWidth() + 10, panel.getHeight()/2);


        //motion
        double deltaX = this.position.x - goalPosition.x;
        double deltaY = this.position.y - goalPosition.y;
        double angle = Math.atan2(deltaY, deltaX) + Math.PI;

        System.out.println("DeltaX: " + deltaX + "\tDeltaY: " + deltaY + "\tAngle: " + angle);

        deltaY = Math.sin(angle) * 100;
        deltaX = Math.cos(angle) * 100;

        System.out.println("DeltaX: " + deltaX + "\tDeltaY: " + deltaY + "\n");

        nextPos.x += deltaX / Constant.ANIMATION_DELAY;
        nextPos.y += deltaY / Constant.ANIMATION_DELAY;


        if(Support.distance(this.position, goalPosition) < Constant.ENTITY_SAFETY_ZONE && !goalsList.isEmpty()){
            this.goalsList.remove(0);
        }

        if(this.position.x > panel.getWidth()){
            this.setGoalsList(new ArrayList<>());
        }

        return new Point(nextPos.x, nextPos.y);
    }

    public Point obstacleAvoidance(List<Obstacle> obstacles, Point newPosition){

        for(int i = 0; i < obstacles.size(); i++){
            if(Support.distance(newPosition, obstacles.get(i).getPosition()) < Constant.ENTITY_SAFETY_ZONE){
                double deltaX = newPosition.x - obstacles.get(i).getPosition().x;
                double deltaY = newPosition.y - obstacles.get(i).getPosition().y;
                double angle = Math.atan2(deltaY, deltaX) + Math.toRadians(180);

                if(Support.getRandomValue(1,10) <= 5)
                    angle += 60;
                else
                    angle -= 60;

                deltaY = Math.sin(angle) * 100;
                deltaX = Math.cos(angle) * 100;

                newPosition.x += deltaX / (Constant.ANIMATION_DELAY);
                newPosition.y += deltaY / (Constant.ANIMATION_DELAY);
            }
        }

        return new Point(newPosition.x, newPosition.y);
    }

    /**
     * Collision avoidance
     * */
    public Point pedestrianAvoidance(List<Pedestrian> crowd, Point newPosition){
        int nPedestrians = crowd.size();

        for(int i = 0; i < nPedestrians; i++){
            if(Support.distance(newPosition, crowd.get(i).getPosition()) < Constant.ENTITY_SAFETY_ZONE){
                double deltaX = newPosition.x - crowd.get(i).getPosition().x;
                double deltaY = newPosition.y - crowd.get(i).getPosition().y;
                double angle = Math.atan2(deltaY, deltaX) + Math.toRadians(180);

                if(Support.getRandomValue(1,10) <= 5)
                    angle += 60;
                else
                    angle -= 60;

                deltaY = Math.sin(angle) * 100;
                deltaX = Math.cos(angle) * 100;

                newPosition.x += deltaX / (Constant.ANIMATION_DELAY);
                newPosition.y += deltaY / (Constant.ANIMATION_DELAY);

            }
        }

        return new Point(newPosition.x, newPosition.y);
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
        goalsNumber = goalsList.size();
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }
}

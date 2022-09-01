package models;

import support.EntityBound;
import support.Support;
import support.constants.Constant;

import processing.core.PVector;
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
    private int energy;

    private Group group;
    private int groupID;
    private List<WayPoint> goalsList;
    private int goalsNumber;
    private List<Obstacle> obstacles;


    /** motion parameters */
    private PVector position;
    private PVector velocity;
    private PVector accelerationVect;
    float radiusVisibility;
    float maxforce;    // Maximum steering force
    float maxspeed;    // Maximum speed
    private EntityBound bounds;


    public Pedestrian(Point2D position, int groupId){
        super(position);
        entityType = Constant.PEDESTRIAN;
        pedestrianShape = new Rectangle2D.Double(position.getX(), position.getY(), Constant.PEDESTRIAN_WIDTH, Constant.PEDESTRIAN_HEIGHT);

        this.groupID = groupId;
        this.goalsNumber = 0;

        this.gender = Support.getRandomValue(Constant.MALE, Constant.FEMALE); //random among MALE and FEMALE
        this.age = Support.getRandomValue(Constant.CHILD, Constant.OLD); //random among CHILD, YOUNG and OLD
        this.energy = assignEnergy();

        //motion parameters
        this.position = new PVector((float)position.getX(), (float)position.getY());
        bounds = new EntityBound(this);
        accelerationVect = new PVector(0, 0);
        velocity = new PVector(1, 1);
        radiusVisibility = (float) Math.PI*2; //default 2.0 (each pedestrian can see the others all around them)
        maxspeed = assignMaxSpeed();
        maxforce = 0.03f;
    }






    /***********************************      PEDESTRIAN MOTION METHODS       *********************************/

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
    public void nextPosition(JPanel panel, ArrayList<Pedestrian> crowd){
        WayPoint nextGoal;

        if(goalsList != null && !goalsList.isEmpty())
            nextGoal = goalsList.get(0);
        else
            nextGoal = new WayPoint(new Point2D.Double(panel.getWidth() + 10, panel.getHeight()/2d));

        //motion
        flock(crowd);
        update();
        updateBounds();

        /*
        * double deltaX = this.position.getX() - goalPosition.getX();
        double deltaY = this.position.getY() - goalPosition.getY();
        double angle = Math.atan2(deltaY, deltaX) + Math.PI;

        deltaY = Math.sin(angle);
        deltaX = Math.cos(angle);

        nextPos = new Point2D.Double(nextPos.getX() + deltaX, nextPos.getY() + deltaY);
        * */


        if(this.checkCollision(nextGoal) && !goalsList.isEmpty()){
            this.goalsList.remove(0);
        }

        if(position.x > panel.getWidth()){
            this.setGoalsList(new ArrayList<>());
        }
    }


    private void applyForce(PVector force) {
        accelerationVect.add(force);
    }


    // We accumulate a new acceleration each time based on three rules
    private void flock(ArrayList<Pedestrian> crowd) {
        PVector sep = separate(crowd);   // Separation
        PVector ali = align(new ArrayList<>(group.getPedestrians()));      // Alignment
        PVector coh = cohesion(new ArrayList<>(group.getPedestrians()));   // Cohesion

        // Arbitrarily weight these forces
        sep.mult(1.0f); //default 1.5
        ali.mult(1.0f); //default 1
        coh.mult(1.0f); //default 1

        // Add the force vectors to acceleration
        applyForce(sep);
        applyForce(ali);
        applyForce(coh);
    }

    // Method to update position
    private void update() {
        // Update velocity
        velocity.add(accelerationVect);

        // Limit speed
        velocity.limit(maxspeed);
        position.add(velocity);

        // Reset accelertion to 0 each cycle
        accelerationVect.mult(0);
    }

    // A method that calculates and applies a steering force towards a target
    // STEER = DESIRED MINUS VELOCITY
    private PVector seek(PVector target) {
        PVector desired = PVector.sub(target, position);  // A vector pointing from the position to the target

        // Scale to maximum speed
        desired.normalize();
        desired.mult(maxspeed);

        // Steering = Desired minus Velocity
        PVector steer = PVector.sub(desired, velocity);
        steer.limit(maxforce);  // Limit to maximum steering force
        return steer;
    }


    // Separation
    // Method checks for nearby pedestrians and steers away
    PVector separate (ArrayList<Pedestrian> pedestrians) {
        float desiredseparation = 25.0f;
        PVector steer = new PVector(0, 0, 0);
        int count = 0;
        // For every boid in the system, check if it's too close
        for (Pedestrian other : pedestrians) {
            float d = PVector.dist(position, other.position);
            // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
            if ((d > 0) && (d < desiredseparation)) {
                // Calculate vector pointing away from neighbor
                PVector diff = PVector.sub(position, other.position);
                diff.normalize();
                diff.div(d);        // Weight by distance
                steer.add(diff);
                count++;            // Keep track of how many
            }
        }
        // Average -- divide by how many
        if (count > 0) {
            steer.div((float)count);
        }

        // As long as the vector is greater than 0
        if (steer.mag() > 0) {

            // Implement Reynolds: Steering = Desired - Velocity
            steer.normalize();
            steer.mult(maxspeed);
            steer.sub(velocity);
            steer.limit(maxforce);
        }
        return steer;
    }


    // Alignment
    // For every nearby pedestrian in the group, calculate the average velocity
    PVector align (ArrayList<Pedestrian> pedestrians) {
        float neighbordist = 50;
        PVector sum = new PVector(0, 0);
        int count = 0;
        for (Pedestrian other : pedestrians) {
            float d = PVector.dist(position, other.position);
            if ((d > 0) && (d < neighbordist)) {
                sum.add(other.velocity);
                count++;
            }
        }
        if (count > 0) {
            sum.div((float)count);

            // Implement Reynolds: Steering = Desired - Velocity
            sum.normalize();
            sum.mult(maxspeed);
            PVector steer = PVector.sub(sum, velocity);
            steer.limit(maxforce);
            return steer;
        }
        else {
            return new PVector(0, 0);
        }
    }

    // Cohesion
    // For the average position (i.e. center) of all nearby boids, calculate steering vector towards that position
    PVector cohesion (ArrayList<Pedestrian> pedestrians) {
        float neighbordist = 50;
        PVector sum = new PVector(0, 0);   // Start with empty vector to accumulate all positions
        int count = 0;
        for (Pedestrian other : pedestrians) {
            float d = PVector.dist(position, other.position);
            if ((d > 0) && (d < neighbordist)) {
                sum.add(other.position); // Add position
                count++;
            }
        }
        if (count > 0) {
            sum.div(count);
            return seek(sum);  // Steer towards the position
        }
        else {
            return new PVector(0, 0);
        }
    }




    /***********************************      PEDESTRIAN'S CHARACTERISTICS      *********************************/

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

    private int assignMaxSpeed(){
        return switch (this.age) {
            case Constant.CHILD, Constant.YOUNG -> Support.getRandomValue(Constant.MIN_VELOCITY, Constant.MAX_VELOCITY);
            case Constant.OLD -> Support.getRandomValue(Constant.MIN_VELOCITY, Constant.MAX_VELOCITY - Constant.MAX_VELOCITY/3);
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
        return new Point2D.Double(this.position.x, this.position.y);
    }

    public int getVelocity() {
        return (int)maxspeed;
    }

    public int getEnergy() {
        return energy;
    }

    public void setGoalsList(List<WayPoint> goalsList) {
        this.goalsList = goalsList;
        goalsNumber = goalsList.size();
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setObstacles(List<Obstacle> obstacles) {
        this.obstacles = obstacles;
    }

    public void updateBounds(){
        this.pedestrianShape = new Rectangle2D.Double(this.position.x, this.position.y, Constant.PEDESTRIAN_WIDTH, Constant.PEDESTRIAN_HEIGHT);
        this.bounds = new EntityBound(this);
    }

}

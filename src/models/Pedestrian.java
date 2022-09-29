package models;

import support.EntityBound;
import support.Support;
import support.constants.Constant;

import processing.core.PVector;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Every Pedestrian is a mobile entity with its own characteristics
 * */
public class Pedestrian extends Entity implements ActionListener {
    private Rectangle2D pedestrianShape;
    private int gender;
    private int age;
    private float energy;
    private float maxEnergy;
    private Group group;
    private int groupID;
    private List<WayPoint> goalsList;
    private WayPoint currentGoal;
    private int goalsNumber;
    private List<Obstacle> obstacles;
    private Building building;
    private boolean isRestTime;
    private boolean hasCollided;


    /** motion parameters */
    private Timer waypointTimer;
    private PVector position;
    private PVector centerPosition;
    private PVector velocity;
    private PVector accelerationVect;
    float maxforce;    // Maximum steering force
    float maxspeed;    // Maximum speed
    private EntityBound entityBounds;
    private Timer energyWasteTimer;


    public Pedestrian(Point2D position, int groupId, Building building){
        super(position);
        entityType = Constant.PEDESTRIAN;
        pedestrianShape = new Rectangle2D.Double(position.getX(), position.getY(), Constant.PEDESTRIAN_WIDTH, Constant.PEDESTRIAN_HEIGHT);
        this.building = building;

        this.groupID = groupId;
        this.goalsNumber = 0;

        this.gender = Support.getRandomValue(Constant.MALE, Constant.FEMALE); //random among MALE and FEMALE
        this.age = Support.getRandomValue(Constant.CHILD, Constant.OLD); //random among CHILD, YOUNG and OLD
        this.energy = assignEnergy();
        this.maxEnergy = energy;
        this.isRestTime = false;
        this.hasCollided = false;

        //motion parameters
        this.position = new PVector((float)position.getX(), (float)position.getY());
        entityBounds = new EntityBound(this);
        this.centerPosition = new PVector((float)entityBounds.getCenter().getX(), (float)entityBounds.getCenter().getY());
        accelerationVect = new PVector(0, 0);
        velocity = new PVector(0, 0);
        maxspeed = assignMaxSpeed();
        maxforce = 0.1f; //default 0.03f

        //timer that decreases and increases energy
        energyWasteTimer = new Timer(1000, e -> handleEnergy());
        energyWasteTimer.start();
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
        //System.out.println("Pedestrian " + this.getVelocity() + "\t Goals: " + this.getGoalPointstoString());
        //System.out.println("Current Goal: " + currentGoal.getWaypointID() + "\n");

        if(currentGoal == null)
            currentGoal = new WayPoint(new Point2D.Double(panel.getWidth() + 10, panel.getHeight()/2d));

        //motion
        flock(crowd);
        update();
        updateBounds();

        if(this.centerPosition.dist(currentGoal.getVectorPosition()) < Constant.GOAL_DISTANCE && !goalsList.isEmpty()){
            if(group.isMoving()) {
                if(currentGoal.getEntityType() == Constant.GENERIC_WAYPOINT) {
                    group.setMoving(false);
                    waypointTimer = new Timer(Support.getRandomValue(Constant.MIN_TIME_FOR_WAYPOINT, Constant.MAX_TIME_FOR_WAYPOINT), this);
                }
                else
                    waypointTimer = new Timer(0, this);
                waypointTimer.start();
            }
        }
    }


    private void applyForce(PVector force) {
        accelerationVect.add(force);
    }


    // We accumulate a new acceleration each time based on some rules, each with its weight
    private void flock(ArrayList<Pedestrian> crowd) {
        PVector sep = separate(crowd);   // Separation between each pedestrian
        PVector coh = cohesion(new ArrayList<>(group.getPedestrians()));   // Cohesion of the group
        PVector dir = direction(); //direction towards the current goal
        PVector avoid = avoidObstacle(); //collision avoidance with obstacles

        // Arbitrarily weight these forces
        weightForces(sep, coh, dir, avoid);

        // Add the force vectors to acceleration
        applyForce(sep);
        applyForce(coh);
        applyForce(dir);
        applyForce(avoid);
    }

    private void weightForces(PVector separation, PVector cohesion, PVector direction, PVector avoidObstacle){
        //default values
        float sepMultiplicator = 2.7f;
        float cohMultiplicator = 1.5f;
        float dirMultiplicator = 1.5f;
        float avoidMultiplicator = 1.5f;

        if(this.position.x < Constant.BUILDING_DISTANCE_LEFT - 10) {
            cohMultiplicator = 2;
            sepMultiplicator = 3;
            dirMultiplicator = 2;
        }

        if(this.hasCollided)
            direction.mult(2);


        separation.mult(sepMultiplicator);
        cohesion.mult(cohMultiplicator);
        direction.mult(dirMultiplicator);
        avoidObstacle.mult(avoidMultiplicator);
    }

    // Method to update position
    private void update() {
        // Update velocity
        velocity.add(accelerationVect);

        // Limit speed
        velocity.limit(maxspeed);

        //walls avoidance
        avoidWall();

        //add vector velocity to position
        position.add(velocity);

        // Reset accelertion to 0 each cycle
        accelerationVect.mult(0);
    }

    // A method that calculates and applies a steering force towards a target
    // STEER = DESIRED MINUS VELOCITY
    private PVector seek(PVector target) {
        PVector desired = PVector.sub(target, centerPosition);  // A vector pointing from the position to the target

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
        float desiredseparation = (float)this.entityBounds.getWidth();
        PVector steer = new PVector(0, 0);
        int count = 0;

        // For every pedestrian in the system, check if it's too close
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


    // Cohesion
    // For the average position (i.e. center) of all nearby boids, calculate steering vector towards that position
    PVector cohesion (ArrayList<Pedestrian> pedestrians) {
        float minAvgNeighborDist = 300; //default = 50 -> what is the avg minimum distance that determine where is the core of the group
        PVector sum = new PVector(0,0);   // Start with empty vector to accumulate all positions
        int count = 0;

        for (Pedestrian other : pedestrians) {
            float d = PVector.dist(centerPosition, other.centerPosition);
            if ((d > 0) && (d < minAvgNeighborDist)) {
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


    // Return a vector toward the current pedestrian's goal
    private PVector direction() {
        return seek(new PVector((float)currentGoal.getPosition().getX(), (float)currentGoal.getPosition().getY()));
    }

    private PVector avoidObstacle(){
        PVector steer = new PVector(0,0);
        float obstacleDist = (float)(this.entityBounds.getWidth() + obstacles.get(0).getEntityBounds().getWidth()/2);

        for(Obstacle obstacle : obstacles){
            PVector obstacleCenter = new PVector((float) obstacle.getEntityBounds().getCenter().getX(), (float) obstacle.getEntityBounds().getCenter().getY());

            if(this.centerPosition.dist(obstacleCenter) < obstacleDist){
                PVector diff = PVector.sub(this.position, obstacleCenter);
                diff.normalize();
                diff.div(this.centerPosition.dist(obstacleCenter));        // Weight by distance
                steer.add(diff);
            }
        }

        return steer;
    }

    private void avoidWall(){
        Line2D wall = building.checkCollision(this);

        if(wall != null) {
            //if the wall is vertical and it is in the middle between the pedestrian and the goal
            if(wall.getX1() == wall.getX2() && wallBeforeGoal(wall, 0)) {
                hasCollided = true;
                velocity = new PVector(0, velocity.y);
            }

            //horizontal wall
            if(wall.getY1() == wall.getY2() && wallBeforeGoal(wall, 1)) {
                hasCollided = true;
                velocity = new PVector(velocity.x, 0);
            }
        }
    }

    private boolean wallBeforeGoal(Line2D wall, int orientation){
        //wall is vertical
        if(orientation == 0){
            if(wall.getX1() > this.getEntityBounds().getCenter().getX() && currentGoal.getPosition().getX() > wall.getX1())
                return true;
        }

        //wall is horizontal
        else{
            if(this.getEntityBounds().getCenter().getY() < wall.getY1() && wall.getY1() < currentGoal.getPosition().getY())
                return true;
            if(this.getEntityBounds().getCenter().getY() > wall.getY1() && wall.getY1() > currentGoal.getPosition().getY())
                return true;
        }

        return false;
    }


    // When a group is done with a waypoint can switch to another
    @Override
    public void actionPerformed(ActionEvent e) {
        if(currentGoal.getEntityType() == Constant.DOOR) {
            if (!this.goalsList.isEmpty()) {
                this.goalsList.remove(0);
                this.setCurrentGoal(this.goalsList.get(0));
            }
        }
        else {
            //if (!this.goalsList.isEmpty())
                //this.goalsList.remove(0);

            if (!this.goalsList.isEmpty()) {
                for (Pedestrian p : group.getPedestrians()) {
                    p.goalsList.remove(currentGoal);
                    p.setCurrentGoal(this.goalsList.get(0));
                }
            }
        }

        /*
        else {
            for(Pedestrian p : group.getPedestrians())
                p.setCurrentGoal(new WayPoint(new Point2D.Double(panel.getWidth() + 100, panel.getHeight() / 2d)));
        }*/

        group.setMoving(true);
        waypointTimer.stop();
    }





    /***********************************      PEDESTRIAN'S CHARACTERISTICS      *********************************/

    private void handleEnergy(){
        if(isRestTime && energy < maxEnergy)
            energy += 1;
        if(!isRestTime){
            switch (this.age) {
                case (Constant.CHILD) -> energy -= 0.15;
                case (Constant.YOUNG) -> energy -= 0.1;
                case (Constant.OLD) -> energy -= 0.2;
            }
        }
        /*
        if(energy < Constant.GO_TO_REST)
            //ricerca waypoint to rest
        */
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

    //velocity between 0.7 and 2.5
    private float assignMaxSpeed(){
        float velocity = 1;
        int counter = Support.getRandomValue(1, 5);
        for(int i = 1; i <= counter; i++) {
            if(Support.getRandomValue(1, 10) < 8)
                velocity = velocity + (0.1f * i);
            else
                velocity = velocity - (0.1f * i);
        }
        if(velocity < 0.7)
            velocity = 0.7f;
        return velocity;
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
        return entityBounds;
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

    public String getPositionString(){
        return "[" + (int)this.position.x + ", " + (int)this.position.y + "]";
    }

    public Rectangle2D getPedestrianShape() {
        return pedestrianShape;
    }

    public Point2D getPosition(){
        return new Point2D.Double(this.position.x, this.position.y);
    }

    public float getVelocity() {
        return maxspeed;
    }

    public float getEnergy() {
        return energy;
    }

    public void setGoalsList(List<WayPoint> goalsList) {
        this.goalsList = new ArrayList<>(goalsList);
        goalsNumber = goalsList.size();
        if(!goalsList.isEmpty())
            currentGoal = goalsList.get(0);
    }

    public List<WayPoint> getGoalsList() {
        return goalsList;
    }

    public Group getGroup() {
        return group;
    }

    public String getGoalPointstoString(){
        String result = "{";

        for(int i = 0; i < goalsList.size(); i++) {
            result += (goalsList.get(i).getWaypointID());
            result += ", ";/*
            if(goalsList.get(i).getWaypointID() != -1) {
                result += (goalsList.get(i).getWaypointID());
                result += ", ";
            }*/
        }

        return result.subSequence(0,result.length()-2) + "}";
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public void setCurrentGoal(WayPoint currentGoal) {
        this.currentGoal = currentGoal;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setObstacles(List<Obstacle> obstacles) {
        this.obstacles = obstacles;
    }

    public void stopWasteEnergyTimer(){
        energyWasteTimer.stop();
    }

    public void startWasteEnergyTimer(){
        energyWasteTimer.start();
    }

    public void updateBounds() {
        this.pedestrianShape = new Rectangle2D.Double(this.position.x, this.position.y, Constant.PEDESTRIAN_WIDTH, Constant.PEDESTRIAN_HEIGHT);
        this.entityBounds = new EntityBound(this);
        this.centerPosition = new PVector((float) entityBounds.getCenter().getX(), (float) entityBounds.getCenter().getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedestrian that = (Pedestrian) o;
        return gender == that.gender && age == that.age && energy == that.energy && groupID == that.groupID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gender, age, energy, groupID);
    }
}

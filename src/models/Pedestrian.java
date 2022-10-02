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
import java.util.Objects;


/**
 * Every Pedestrian is a mobile entity with its own characteristics
 * */
public class Pedestrian extends Entity {
    private Rectangle2D pedestrianShape;
    private int gender;
    private int age;
    private float energy;
    private float maxEnergy;
    private Group group;
    private int groupID;
    private List<WayPoint> goalsList;
    private WayPoint currentGoal;
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
        currentGoal = building.getEntrance();

        //timer that decreases and increases energy
        energyWasteTimer = new Timer(1000, e -> handleEnergy());
        energyWasteTimer.start();
    }






    /***********************************      PEDESTRIAN MOTION METHODS       *********************************/

    /**
     * Compute next position towards a goal, it ignores obstacles and other pedestrians
     * */
    public void nextPosition(JPanel panel, ArrayList<Pedestrian> crowd){
        if(currentGoal == null)
            currentGoal = new WayPoint(new Point2D.Double(panel.getWidth() + 10, panel.getHeight()/2d));

        //motion
        flock(crowd);
        update();
        updateBounds();

        if(currentGoal.getEntityType() == Constant.DOOR && currentGoal.getShape().contains(this.entityBounds.getCenter())){
            updateCurrentGoalAfterDoor();
        }

        if(currentGoal.getEntityType() == Constant.GENERIC_WAYPOINT && this.centerPosition.dist(currentGoal.getVectorPosition()) < Constant.GOAL_DISTANCE){
            if(group.isMoving()) {
                group.setMoving(false);
                waypointTimer = new Timer(Support.getRandomValue(Constant.MIN_TIME_FOR_WAYPOINT, Constant.MAX_TIME_FOR_WAYPOINT), e -> updateCurrentGoal());
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

        //if this pedestrian is out of the building
        if(this.position.x < Constant.BUILDING_DISTANCE_LEFT - 10) {
            cohMultiplicator = 2;
            sepMultiplicator = 3;
            dirMultiplicator = 1.6f;
        }

        //if the pedestrian has collided
        if(this.hasCollided)
            direction.mult(4);

        //if the pedestrian is far from the group
        if(this.group.getPedestrians().size() > 1) {
            if (this.position.dist(group.getAvgGroupPosition()) > Constant.PEDESTRIAN_HEIGHT * this.group.getPedestrians().size()) {
                cohMultiplicator = 2;
                sepMultiplicator = 3;
                dirMultiplicator = 1.6f;
            }
        }

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
    // For the average position (i.e. center) of all the members of a group, calculate steering vector towards that position
    PVector cohesion (ArrayList<Pedestrian> pedestrians) {
        PVector sum = new PVector(0,0);   // Start with empty vector to accumulate all positions
        int count = 0;

        for (Pedestrian other : pedestrians) {
            float d = PVector.dist(centerPosition, other.centerPosition);
            if (d > 0) {
                sum.add(other.position); // Add position
                this.group.setAvgGroupPosition(sum);
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
                diff.div(this.centerPosition.dist(obstacleCenter)/2);        // Weight by distance
                steer.add(diff);
            }
        }

        return steer;
    }

    private void avoidWall(){
        List<Integer> collidedPoints = building.collisionPoints(this);

        if(!collidedPoints.isEmpty()){
            if(collidedPoints.contains(Constant.UP)){
                hasCollided = true;
                if(velocity.y < 0) //if the pedestrian is trying to go higher on the screen, it cannot anymore
                    velocity = new PVector(velocity.x, 0);
            }

            if(collidedPoints.contains(Constant.BOTTOM)){
                hasCollided = true;
                if(velocity.y > 0) //if the pedestrian is trying to go down on the screen, it cannot anymore
                    velocity = new PVector(velocity.x, 0);
            }

            if(collidedPoints.contains(Constant.LEFT)){
                hasCollided = true;
                if(velocity.x < 0) //if the pedestrian is trying to go towards left, it cannot anymore
                    velocity = new PVector(0, velocity.y);
            }

            if(collidedPoints.contains(Constant.RIGHT)){
                hasCollided = true;
                if(velocity.x > 0) //if the pedestrian is trying to go towards right, it cannot anymore
                    velocity = new PVector(0, velocity.y);
            }
        }
        else
            hasCollided = false;
    }


    /**
     * When a pedestrian reach a door this algorithm compute the next goal or the next door
     * */
    private void updateCurrentGoalAfterDoor(){
        if(!goalsList.isEmpty()) {

            //if the next goal is in a room (where is not the pedestrian), set the door of that room as the currentGoal
            if(goalsList.get(0).getRoom() != null && !goalsList.get(0).getRoom().getRoomRectangle().intersects(this.getPedestrianShape())){
                currentGoal = goalsList.get(0).getRoom().getDoor();
            }
            else {
                currentGoal = goalsList.get(0);
            }
        }
    }

    /**
     * When a pedestrian is done with a certain waypoint, this algorithm compute the next goal or the next door
     * */
    public void updateCurrentGoal() {
        Room pedestrianRoom = currentGoal.getRoom();

        if(!goalsList.isEmpty())
            group.removeFirstGoal();
        currentGoal = goalsList.get(0);

        //Pedestrian in a room and next goal in a place that is not the same room
        if (pedestrianRoom != null && currentGoal.getRoom() != pedestrianRoom)
            currentGoal = pedestrianRoom.getDoor();

        //Pedestrian in the hall and next goal in a room
        if(pedestrianRoom == null && currentGoal.getRoom() != null)
            currentGoal = currentGoal.getRoom().getDoor();

        group.setMoving(true);
        waypointTimer.stop();
    }

    /**
     * Function called to make sure that this pedestrian is not getting lost around the building,
     * every x milliseconds check the goal list and update the path to follow
     * */
    public void updatePath(){
        if(currentGoal != goalsList.get(0) && currentGoal.getEntityType() != Constant.DOOR){
            currentGoal = goalsList.get(0);
        }
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
        this.goalsList = goalsList;
    }

    public List<WayPoint> getGoalsList() {
        return goalsList;
    }

    public Group getGroup() {
        return group;
    }

    public String getGoalPointstoString(){
        String result = "{";

        for (WayPoint wayPoint : goalsList) {
            result += (wayPoint.getWaypointID());
            result += ", ";
        }

        return result.subSequence(0,result.length()-2) + "}";
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
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

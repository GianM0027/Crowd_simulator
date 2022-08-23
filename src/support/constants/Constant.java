package support.constants;

public class Constant {
    // entities constant
    public static final int PEDESTRIAN = -1;
    public static final int OBSTACLE = -2;
    public static final int WAY_POINT = -3;
    public static final int PEDESTRIAN_SIZE = 6;
    public static final int OBSTACLE_SIZE = 12;
    public static final int WAYPOINT_SIZE = 3;

    //pedestrian constants
    public static final int MALE = 1;
    public static final int FEMALE = 2;
    public static final int CHILD = 3;
    public static final int YOUNG = 4;
    public static final int OLD = 5;
    public static final int MIN_VELOCITY = 2;
    public static final int MAX_VELOCITY = 6;
    public static final int MIN_ENERGY_CHILD = 40;
    public static final int MAX_ENERGY_CHILD = 50;
    public static final int MIN_ENERGY_YOUNG = 30;
    public static final int MAX_ENERGY_YOUNG = 50;
    public static final int MIN_ENERGY_OLD = 20;
    public static final int MAX_ENERGY_OLD = 40;


    /** animation's constants */
    public static final int BOUNDS_DISTANCE = PEDESTRIAN_SIZE /2;
    public static final int BUILDING_STROKE = 3;
    public static final int BUILDING_DOOR_SIZE = 40;
    public static final int BUILDING_DISTANCE_UP_DOWN = 10; //not lower than 10
    public static final int BUILDING_DISTANCE_LEFT = 200;
    public static final int BUILDING_DISTANCE_RIGHT = 20;
    public static final int ANIMATION_DELAY = 40;

}

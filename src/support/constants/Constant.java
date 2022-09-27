package support.constants;

public class Constant {

    // entities constant
    public static final int PEDESTRIAN = -1;
    public static final int OBSTACLE = -2;
    public static final int PEDESTRIAN_WIDTH = 6;
    public static final int PEDESTRIAN_HEIGHT = 12;
    public static final int OBSTACLE_WIDTH = 12;
    public static final int OBSTACLE_HEIGHT = 12;
    public static final int WAYPOINT_WIDTH = 6;
    public static final int WAYPOINT_HEIGHT = 6;

    
    //pedestrian constants
    public static final int MALE = 1;
    public static final int FEMALE = 2;
    public static final int CHILD = 3;
    public static final int YOUNG = 4;
    public static final int OLD = 5;
    public static final int MIN_VELOCITY = 0;
    public static final int MAX_VELOCITY = 3;
    public static final int MIN_ENERGY_CHILD = 40;
    public static final int MAX_ENERGY_CHILD = 50;
    public static final int MIN_ENERGY_YOUNG = 30;
    public static final int MAX_ENERGY_YOUNG = 50;
    public static final int MIN_ENERGY_OLD = 20;
    public static final int MAX_ENERGY_OLD = 40;
    public static final int MIN_GROUPS_SIZE = 1;
    public static final int MAX_GROUPS_SIZE = 15;
    public static final int GO_TO_REST = 10;
    public static final int MAX_SEE_AHEAD = 15;



    // animation's constants
    public static final int MIN_TIME_FOR_WAYPOINT = 3000; //10000
    public static final int MAX_TIME_FOR_WAYPOINT = 7000; //15000
    public static final int BOUNDS_DISTANCE = PEDESTRIAN_WIDTH /2;
    public static final int BUILDING_STROKE = 4;
    public static final int BUILDING_DOOR_SIZE = PEDESTRIAN_WIDTH *5;
    public static final int BUILDING_DISTANCE_UP_DOWN = 10; //not lower than 10
    public static final int BUILDING_DISTANCE_LEFT = 200;
    public static final int BUILDING_DISTANCE_RIGHT = 20;
    public static final int ANIMATION_DELAY = 40; //default 40

    //Waypoints constants
    public static final int GENERIC_WAYPOINT = -3;
    public static final int DOOR = -4;
    public static final int GOAL_DISTANCE = BUILDING_DOOR_SIZE/3;

}

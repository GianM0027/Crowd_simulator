package support.dataHandler;

import com.google.gson.annotations.Expose;
import models.Pedestrian;

/**
 * Given a pedestrians, this class takes only the useful information about it and organize the timestamp for that
 * pedestrian in a given moment
 */
public class PedestrianTimestamp {
    @Expose
    private float central_position_x;
    @Expose
    private float central_position_y;
    @Expose
    private boolean leftTheBuilding;
    @Expose
    private int currentGoal;
    @Expose
    private float time;

    public PedestrianTimestamp(Pedestrian pedestrian, float time){
        this.central_position_x = pedestrian.getCenterPosition().x;
        this.central_position_y = pedestrian.getCenterPosition().y;
        this.leftTheBuilding = pedestrian.hasLeftTheBuilding();
        this.currentGoal = pedestrian.getCurrentGoal().getWaypointID();
        this.time = time;
    }
}

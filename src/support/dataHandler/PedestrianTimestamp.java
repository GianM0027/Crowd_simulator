package support.dataHandler;

import com.google.gson.annotations.Expose;
import models.Pedestrian;

public class PedestrianTimestamp {
    @Expose
    private float central_position_x;
    @Expose
    private float central_position_y;
    //@Expose
    private float velocity;
    @Expose
    private int currentGoal;
    @Expose
    private float time;

    public PedestrianTimestamp(Pedestrian pedestrian, float time){
        this.central_position_x = pedestrian.getCenterPosition().x;
        this.central_position_y = pedestrian.getCenterPosition().y;
        this.velocity = pedestrian.getCurrentVelocity().mag();
        this.currentGoal = pedestrian.getCurrentGoal().getWaypointID();
        this.time = time;
    }
}

package support.dataHandler;

import com.google.gson.annotations.Expose;
import models.Group;
import models.Pedestrian;

/**
 * Given a group of pedestrians, this class takes only the useful information about it and organize the timestamp for that
 * group in a given moment
 */
public class GroupTimestamp {
    @Expose
    private boolean leftTheBuilding;
    @Expose
    private boolean isVisitingAWaypoint;
    @Expose
    private boolean isResting;
    @Expose
    private boolean hasStartedWalking;
    @Expose
    private float time;

    public GroupTimestamp(Group group, float time){
        this.time = time;
        leftTheBuilding = true;
        for(Pedestrian pedestrian : group.getPedestrians())
            if(!pedestrian.hasLeftTheBuilding())
                leftTheBuilding = false;

        this.isVisitingAWaypoint = !group.isMoving();
        this.isResting = group.isRestTime();
        this.hasStartedWalking = group.hasStartedWalking();
    }
}

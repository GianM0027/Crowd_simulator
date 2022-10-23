package support.dataHandler;

import com.google.gson.annotations.Expose;
import models.Group;
import models.Pedestrian;

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

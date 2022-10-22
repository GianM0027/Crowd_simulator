package support.dataHandler;

import com.google.gson.annotations.Expose;
import models.Group;

public class GroupTimestamp {
    @Expose
    private boolean isVisitingAWaypoint;
    @Expose
    private boolean isResting;
    @Expose
    private boolean hasStartedWalking;
    @Expose
    private boolean time;

    public GroupTimestamp(Group group, float time){
        this.isVisitingAWaypoint = !group.isMoving();
        this.isResting = group.isRestTime();
        this.hasStartedWalking = group.hasStartedWalking();
    }
}

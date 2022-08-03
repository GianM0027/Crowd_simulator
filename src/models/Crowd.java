package models;

import java.util.List;

public class Crowd {

    private List<Pedestrian> crowd;
    private List<Group> groups;
    private int crowdSize;
    private int groupsAmount;

    public Crowd(int size, int numOfGroups){
        this.crowdSize = size;
        this.groupsAmount = numOfGroups;
    }




    public List<Pedestrian> getCrowd() {
        return crowd;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public int getCrowdSize() {
        return crowdSize;
    }

    public int getGroupsAmount() {
        return groupsAmount;
    }
}



package support.dataHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Group;
import models.Obstacle;
import models.Pedestrian;
import models.WayPoint;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * This class allows collecting, organizing and storing data from the simulation in a file in JSON format. It has a list
 * for each entity and a list of timestamps for each dynamic entity
 */
public class DataHandler{
    private Gson gson;
    private List<String> groupsStaticData;
    private List<String> obstaclesData;
    private List<String> waypointsData;
    private List<String> pedestriansStaticData;
    private List<List<String>> pedestriansTimestamp;
    private List<List<String>> groupsTimestamp;
    private int pedestriansTimestampNumber;
    private int groupsTimestampNumber;

    public DataHandler(){
        GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();

        obstaclesData = new ArrayList<>();
        waypointsData = new ArrayList<>();
        groupsStaticData = new ArrayList<>();
        groupsTimestamp = new ArrayList<>();
        pedestriansStaticData = new ArrayList<>();
        pedestriansTimestamp = new ArrayList<>();
        pedestriansTimestampNumber = 0;
        groupsTimestampNumber = 0;
    }


    /**
     * Function called at the start of the simulation, it collects static data from the list of obstacles in the environment
     * @param obstaclesData
     */
    public void setObstaclesData(List<Obstacle> obstaclesData) {
        int n = 1;
        for(int i = 0; i < obstaclesData.size(); i++){
            String jsonString = gson.toJson(obstaclesData.get(i));
            if(i != obstaclesData.size()-1)
                this.obstaclesData.add("\"obstacle " + n + "\":" + jsonString + ",");
            else
                this.obstaclesData.add("\"obstacle " + n + "\":" + jsonString);
            n++;
        }
    }

    /**
     * Function called at the start of the simulation, it collects static data from the list of waypoints in the environment
     * @param waypointsData
     */
    public void setWaypointsData(List<WayPoint> waypointsData) {
        int n = 1;
        for(int i = 0; i < waypointsData.size(); i++){
            String jsonString = gson.toJson(waypointsData.get(i));
            if(i != waypointsData.size()-1)
                this.waypointsData.add("\"waypoint " + n + "\":" + jsonString + ",");
            else
                this.waypointsData.add("\"waypoint " + n + "\":" + jsonString);
            n++;
        }
    }

    /**
     * Function called at the start of the simulation, it collects static data from the list of groups in the environment
     * @param groupsStaticData
     */
    public void setGroupsStaticData(List<Group> groupsStaticData) {
        for(int i = 0; i < groupsStaticData.size(); i++){
            String jsonString = gson.toJson(groupsStaticData.get(i));
            this.groupsStaticData.add("\"group " + i + "\":" + jsonString.substring(0, jsonString.length()-1) + ",");
        }

        //initialising matrix of timestamp
        for(int i = 0; i < groupsStaticData.size(); i++)
            groupsTimestamp.add(new ArrayList<>());
    }

    /**
     * Function called at the start of the simulation, it collects static data from the list of pedestrians in the environment
     * @param pedestriansStaticData
     */
    public void setPedestriansStaticData(List<Pedestrian> pedestriansStaticData) {
        for(int i = 0; i < pedestriansStaticData.size(); i++){
            String jsonString = gson.toJson(pedestriansStaticData.get(i));
            this.pedestriansStaticData.add("\"pedestrian " + i + "\":" + jsonString.substring(0, jsonString.length()-1) + ",");
        }

        //initialising matrix of timestamp
        for(int i = 0; i < pedestriansStaticData.size(); i++)
            pedestriansTimestamp.add(new ArrayList<>());
    }

    /**
     * Function called at regular intervals, for each time it collects and stores the dynamic data of pedestrians
     * @param pedestrians the list of pedestrians in the environment
     * @param time is the time the collected information belong
     */
    public void setPedestriansTimestamp(List<Pedestrian> pedestrians, float time) {
        for(int i = 0; i < pedestrians.size(); i++){
            PedestrianTimestamp timestamp = new PedestrianTimestamp(pedestrians.get(i), time);
            String jsonString = gson.toJson(timestamp);
            this.pedestriansTimestamp.get(i).add(pedestriansTimestampNumber, jsonString);
        }
        pedestriansTimestampNumber++;
    }

    /**
     * Function called at regular intervals, for each time it collects and stores the dynamic data of groups
     * @param groups the list of groups in the environment
     * @param time is the time the collected information belong
     */
    public void setGroupsTimestamp(List<Group> groups, float time){
        for(int i = 0; i < groups.size(); i++){
            GroupTimestamp timestamp = new GroupTimestamp(groups.get(i), time);
            String jsonString = gson.toJson(timestamp);
            this.groupsTimestamp.get(i).add(groupsTimestampNumber, jsonString);
        }
        groupsTimestampNumber++;
    }

    /**
     * This function is called at the end of a simulation, it creates the output file (currently it replaces the existing file every time)
     * in JSON syntax with the static and dynamic data of the entities
     * @throws IOException
     */
    public void simulationDataToJSON() throws IOException {
        PrintWriter writer = new PrintWriter("simulation data.txt", StandardCharsets.UTF_8);

        //create the file
        writer.println("{");

        writer.println("\"Obstacles\": [{");
        for(String s : obstaclesData)
            writer.println(s);
        writer.println("}],");

        writer.println("\"Waypoints\": [{");
        for(String s : waypointsData)
            writer.println(s);
        writer.println("}],");

        writer.println("\"Groups\": [{");
        for(int i = 0; i < groupsStaticData.size(); i++){
            writer.println(groupsStaticData.get(i));
            writeGrupsTimestamp(writer, i);
        }
        writer.println("}],");

        writer.println("\"Pedestrians\": [{");
        for(int i = 0; i < pedestriansStaticData.size(); i++){
            writer.println(pedestriansStaticData.get(i));
            writePedestrianTimestamp(writer, i);
        }
        writer.println("}]");

        writer.println("}");
        writer.close();
    }

    private void writePedestrianTimestamp(PrintWriter writer, int row){
        writer.println("\"timestamp " + row + "\": [");
        for(int column = 0; column < pedestriansTimestampNumber; column++){
            if(column != pedestriansTimestampNumber-1)
                writer.println(pedestriansTimestamp.get(row).get(column) + ",");
            else
                writer.println(pedestriansTimestamp.get(row).get(column));
        }
        writer.println("]");

        if(row != pedestriansStaticData.size()-1)
            writer.println("},");
        else
            writer.println("}");
    }

    private void writeGrupsTimestamp(PrintWriter writer, int row){
        writer.println("\"timestamp " + row + "\": [");
        for(int column = 0; column < groupsTimestampNumber; column++){
            if(column != groupsTimestampNumber-1)
                writer.println(groupsTimestamp.get(row).get(column) + ",");
            else
                writer.println(groupsTimestamp.get(row).get(column));
        }
        writer.println("]");

        if(row != groupsStaticData.size()-1)
            writer.println("},");
        else
            writer.println("}");
    }

}

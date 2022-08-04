package support;

import models.*;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Support functions indipendet from specific classes
 * */
public class Support {

    public static void sortObstacles(ArrayList<Obstacle> list){
        int n = list.size();

        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i + 1; j < n; j++)
                if (list.get(j).getPosition().getX() < list.get(min_idx).getPosition().getX())
                    min_idx = j;

            // Swap the found minimum element with the first element
            Obstacle temp = list.get(min_idx);
            list.set(min_idx, list.get(i));
            list.set(i, temp);
        }
    }

    public static void sortWayPoints(ArrayList<WayPoint> list) {
        int n = list.size();

        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i + 1; j < n; j++)
                if (list.get(j).getPosition().getX() < list.get(min_idx).getPosition().getX())
                    min_idx = j;

            // Swap the found minimum element with the first element
            WayPoint temp = list.get(min_idx);
            list.set(min_idx, list.get(i));
            list.set(i, temp);
        }
    }

    public static void sortPedestriansByAge(ArrayList<Pedestrian> list){
        int n = list.size();

        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i + 1; j < n; j++)
                if (list.get(j).getAge() < list.get(min_idx).getAge())
                    min_idx = j;

            // Swap the found minimum element with the first element
            Pedestrian temp = list.get(min_idx);
            list.set(min_idx, list.get(i));
            list.set(i, temp);
        }
    }

    public static void sortPedestriansByGender(ArrayList<Pedestrian> list){
        int n = list.size();

        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i + 1; j < n; j++)
                if (list.get(j).getGender() < list.get(min_idx).getGender())
                    min_idx = j;

            // Swap the found minimum element with the first element
            Pedestrian temp = list.get(min_idx);
            list.set(min_idx, list.get(i));
            list.set(i, temp);
        }
    }
    public static void sortPedestriansByVelocity(ArrayList<Pedestrian> list){
        int n = list.size();

        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i + 1; j < n; j++)
                if (list.get(j).getVelocity() < list.get(min_idx).getVelocity())
                    min_idx = j;

            // Swap the found minimum element with the first element
            Pedestrian temp = list.get(min_idx);
            list.set(min_idx, list.get(i));
            list.set(i, temp);
        }
    }
    public static void sortPedestriansByEnergy(ArrayList<Pedestrian> list){
        int n = list.size();

        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i + 1; j < n; j++)
                if (list.get(j).getEnergy() < list.get(min_idx).getEnergy())
                    min_idx = j;

            // Swap the found minimum element with the first element
            Pedestrian temp = list.get(min_idx);
            list.set(min_idx, list.get(i));
            list.set(i, temp);
        }
    }



    public static int getRandomValue(int Min, int Max) {
        // Get and return the random integer
        // within Min and Max
        return ThreadLocalRandom.current().nextInt(Min, Max + 1);
    }
}

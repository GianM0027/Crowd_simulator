package support;

import models.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Support functions independent of specific classes
 * */
public abstract class Support {

    //distance between two points
    public static double distance(Point p1, Point p2){
        return Math.sqrt(((p2.y - p1.y)*(p2.y - p1.y)) + ((p2.x - p1.x)*(p2.x - p1.x)));
    }


    /**********************************   SORTING FUNCTIONS   **********************************/
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


    public static int getRandomValue(int Min, int Max) {
        // Get and return the random integer
        // within Min and Max
        return ThreadLocalRandom.current().nextInt(Min, Max + 1);
    }



    ///////////////////////// Pedestrian sorting functions ////////////////////////////////////
    static int partitionVelocity(ArrayList<Pedestrian> list, int low, int high) {
        Pedestrian pivot = list.get(high);
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (list.get(j).getVelocity() <= pivot.getVelocity()) {
                i++;

                Pedestrian temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }

        Pedestrian temp = list.get(i+1);
        list.set(i+1, list.get(high));
        list.set(high, temp);

        return i+1;
    }

    /* The main function that implements QuickSort
              arr[] --> Array to be sorted,
              low --> Starting index,
              high --> Ending index
     */
    public static void quickSortByVelocity(ArrayList<Pedestrian> list, int low, int high) {
        if (low < high) {

            // pi is partitioning index, arr[p] is now at right place
            int pi = partitionVelocity(list, low, high);

            // Separately sort elements before partition and after partition
            quickSortByVelocity(list, low, pi - 1);
            quickSortByVelocity(list, pi + 1, high);
        }
    }

    static int partitionEnergy(ArrayList<Pedestrian> list, int low, int high) {
        Pedestrian pivot = list.get(high);
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (list.get(j).getEnergy() <= pivot.getEnergy()) {
                i++;

                Pedestrian temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }

        Pedestrian temp = list.get(i+1);
        list.set(i+1, list.get(high));
        list.set(high, temp);

        return i+1;
    }

    /* The main function that implements QuickSort
              arr[] --> Array to be sorted,
              low --> Starting index,
              high --> Ending index
     */
    public static void quickSortByEnergy(ArrayList<Pedestrian> list, int low, int high) {
        if (low < high) {

            // pi is partitioning index, arr[p] is now at right place
            int pi = partitionEnergy(list, low, high);

            // Separately sort elements before partition and after partition
            quickSortByEnergy(list, low, pi - 1);
            quickSortByEnergy(list, pi + 1, high);
        }
    }

    public static void sortByAge(ArrayList<Pedestrian> list, int low, int high) {
        for(int i = 0; i < list.size(); ++i){

            int j = i;

            while(j > 0 && list.get(j-1).getAge() > list.get(j).getAge()){
                Pedestrian temp = list.get(j);
                list.set(j, list.get(j-1));
                list.set(j - 1, temp);

                j = j-1;
            }
        }
    }


    public static void sortByGender(ArrayList<Pedestrian> list, int low, int high) {
        for(int i = 0; i < list.size(); ++i){

            int j = i;

            while(j > 0 && list.get(j-1).getGender() > list.get(j).getGender()){
                Pedestrian temp = list.get(j);
                list.set(j, list.get(j-1));
                list.set(j - 1, temp);

                j = j-1;
            }
        }
    }

}

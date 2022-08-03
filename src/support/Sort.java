package support;

import models.Obstacle;
import models.WayPoint;

import java.util.ArrayList;

public class Sort {

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
}

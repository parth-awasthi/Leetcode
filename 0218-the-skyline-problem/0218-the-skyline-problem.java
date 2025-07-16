import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

class Solution {
    /**
     * Computes the skyline contour from a list of buildings.
     *
     * @param buildings An array where each element is [left, right, height].
     * @return A list of key points [x, height] representing the skyline.
     */
    public List<List<Integer>> getSkyline(int[][] buildings) {
        // Step 1: Create a list of "critical points" from the buildings.
        // A building [L, R, H] is split into two points:
        // - A start point [L, -H]
        // - An end point [R, H]
        // Using a negative height for start points is a trick to help with sorting.
        List<int[]> points = new ArrayList<>();
        for (int[] b : buildings) {
            points.add(new int[]{b[0], -b[2]}); // Start of a building
            points.add(new int[]{b[1], b[2]});  // End of a building
        }

        // Step 2: Sort the critical points.
        // The sorting order is crucial for the sweep-line algorithm to work correctly.
        // - Primary sort by x-coordinate (left to right).
        // - If x-coordinates are the same, sort by height:
        //   1. Start points (-H) will come before end points (H).
        //   2. For two start points, the taller one (-H is smaller) comes first.
        //   3. For two end points, the shorter one comes first.
        Collections.sort(points, (a, b) -> {
            if (a[0] != b[0]) {
                return a[0] - b[0];
            } else {
                return a[1] - b[1];
            }
        });

        // Step 3: Process the points using a sweep-line.
        // We use a TreeMap as a max-heap to keep track of the heights of "active" buildings.
        // Key: height, Value: count of buildings with that height.
        TreeMap<Integer, Integer> activeHeights = new TreeMap<>();
        activeHeights.put(0, 1); // Add ground level height of 0.

        int prevMaxHeight = 0;
        List<List<Integer>> skyline = new ArrayList<>();
        
        int i = 0;
        while (i < points.size()) {
            int currentX = points.get(i)[0];
            
            // Process all points that share the same x-coordinate in one go.
            int j = i;
            while (j < points.size() && points.get(j)[0] == currentX) {
                int[] p = points.get(j);
                int height = Math.abs(p[1]);
                boolean isStart = p[1] < 0;

                if (isStart) {
                    // If it's a start point, add its height to the map.
                    activeHeights.put(height, activeHeights.getOrDefault(height, 0) + 1);
                } else { // It's an end point.
                    // If it's an end point, decrease its height's count.
                    activeHeights.put(height, activeHeights.get(height) - 1);
                    // If the count becomes zero, remove it from the map.
                    if (activeHeights.get(height) == 0) {
                        activeHeights.remove(height);
                    }
                }
                j++;
            }
            
            // After processing all changes at currentX, find the new max height.
            // With TreeMap, the max key is the max height.
            int currentMaxHeight = activeHeights.lastKey();
            
            // If the max height has changed, it's a key point of the skyline.
            if (prevMaxHeight != currentMaxHeight) {
                skyline.add(Arrays.asList(currentX, currentMaxHeight));
                prevMaxHeight = currentMaxHeight;
            }
            
            // Move our main index to the next distinct x-coordinate.
            i = j;
        }
        
        return skyline;
    }
}
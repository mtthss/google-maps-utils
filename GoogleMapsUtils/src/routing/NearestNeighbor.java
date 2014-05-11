package routing;

public class NearestNeighbor {
        
        private static final Tool tool = new Tool();
        
        public static int[] executeNearestNeighbor(final int[][] distanceMatrix, final int startCity) {
                
                int[] path = new int[distanceMatrix[0].length];
                
                path[0] = startCity;
                int currentCity = startCity;
                
                int i = 1;
                while (i < path.length) {
                        
                        int nextCity = findMin(distanceMatrix[currentCity], path);
                        
                        if(nextCity != -1) {
                                path[i] = nextCity;
                                currentCity = nextCity;
                                i++;
                        }
                }
                
                return path;
        }
        
        /**
         * Find the nearest city that has not yet been visited
         * @param row
         * @return next city to visit
         */
        private static int findMin(int[] row, int[] path) {
                
                int nextCity = -1;
                int i = 0;
                int min = Integer.MAX_VALUE;
                
                while(i < row.length)  {
                        if(tool.isCityInPath(path, i) == false && row[i] < min) {
                                min = row[i];
                                nextCity = i;
                        }
                        i++;
                }
                return nextCity;
        }

}

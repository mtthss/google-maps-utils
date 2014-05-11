package routing;
public class Tool {

	public int computeCost(int[] path, int[][] distanceMatrix) {
		int cost = 0;
        for(int i = 1; i < path.length; i++) {
        	cost += distanceMatrix[path[i-1]][path[i]];
        }
        cost += distanceMatrix[path[path.length - 1]] [path[0]];
        return cost;
    }

    public int getDestination(int[] path, int srcIndex) {
        if(srcIndex + 1 == path.length) {
        	return path[0];
        }
        return path[srcIndex + 1];
    }
        
    public int getIndexOfDestination(int[] path, int srcIndex) {
    	if(srcIndex + 1 == path.length) {
    		return 0;
        }
        return srcIndex + 1;
    }       
        
    public boolean isCityInPath(int[] path, int city) {
    	for(int i = 0; i < path.length; i++) {
    		if(path[i] == city) {
    			return true;
            }
        }
        return false;
    }
}

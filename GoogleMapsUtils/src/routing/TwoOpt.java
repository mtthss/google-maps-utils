package routing;

public class TwoOpt {

	private int[] path;
	private int[][] distanceMatrix;
    private Tool tool = new Tool();
        
    public TwoOpt(int[] path, int[][] distanceMatrix) {
    	this.distanceMatrix = distanceMatrix;
        this.path = path;
                
        int bestGain = Integer.MAX_VALUE;
        int bestI = Integer.MAX_VALUE;
        int bestJ = Integer.MAX_VALUE;
                
        while(bestGain >= 0) {
        	bestGain = 0;
            for(int i = 0; i < path.length; i++) {
            	for(int j = 0; j < path.length; j++) {
            		if(i!=j) {
            			int gain = computeGain(i, j);
                        if(gain < bestGain) {
                        	bestGain = gain;
                            bestI = i;
                            bestJ = j;
                        }
                    }
                }
            }
                        
            if(bestI != Integer.MAX_VALUE && bestJ != Integer.MAX_VALUE) {
            	exchange(bestI, bestJ);
            }
        }
    }
        
    private int computeGain(final int cityIndex1, final int cityIndex2) {
                
    	int src1 = path[cityIndex1];
    	int src2 = path[cityIndex2];
                
    	int dest1 = tool.getDestination(path, cityIndex1);
    	int dest2 = tool.getDestination(path, cityIndex2);;
                
    	return ((distanceMatrix[dest1][dest2] + distanceMatrix[src1][src2]) - (distanceMatrix[src1][dest1] + distanceMatrix[src2][dest2]));
    }
        
    private void exchange(final int cityIndex1, final int cityIndex2) {
                
    	int indexDest1 = tool.getIndexOfDestination(path, cityIndex1);
        int indexDest2 = tool.getIndexOfDestination(path, cityIndex2);
        
        int[] pathNew = new int[path.length];
        int indexOfPathNew = 0;
                
        int i = 0;
        while(i <= cityIndex1) {
        	if(tool.isCityInPath(pathNew, path[i]) == false) {
        		pathNew[indexOfPathNew] = path[i];
                indexOfPathNew++;
            }
            i++;
        }
                
        i = cityIndex2;
        while(i >= indexDest1) {
        	if(tool.isCityInPath(pathNew, path[i]) == false) {
        		pathNew[indexOfPathNew] = path[i];
                indexOfPathNew++;
            }
            i--;
        }
                
        i = indexDest2;
        while(i < path.length) {
        	if(tool.isCityInPath(pathNew, path[i]) == false) {
        		pathNew[indexOfPathNew] = path[i];
                indexOfPathNew++;
            }
            i++;
        }
                
        for(int j = 0; j < pathNew.length; j++) {
        	path[j] = pathNew[j];
        }
    }
        
    public int[] getPath() {
    	return path;
    }
                                                  
}
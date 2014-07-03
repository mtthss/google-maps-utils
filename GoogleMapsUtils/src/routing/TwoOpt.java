package routing;

public class TwoOpt {

	private int[] path;
	private int[] pathNew;
	private int[][] distanceMatrix;
    private Tool tool = new Tool();
    private int bestCost;
    private int improvedCost;
    
    
    public TwoOpt(int[] initialPath, int[][] distanceMatrix) {
    	
    	this.distanceMatrix = distanceMatrix;
        this.path = initialPath;
        this.bestCost = tool.computeCost(path, distanceMatrix);
        this.improvedCost = bestCost;
        
        boolean isImproved = true;
        int iterations = 0;
        
        while(isImproved && iterations < 30){
        	isImproved = false;
        	for(int i = 1; i < path.length && !isImproved; i++) {
            	for(int j = i+1; j < path.length && !isImproved; j++) {
            		if(i!=j) {
            			improvedCost = tryExchange(i, j);
            			if(improvedCost<bestCost){
            				path = pathNew;
            				bestCost = improvedCost;
            				isImproved = true;
            			}
            		}
            	}
            }
        	iterations++;
        }        
        
    }
    
    private int tryExchange(int index1, int index2){
        
        pathNew = new int[path.length];
        int indexOfPathNew = 0;
                
        int i = 0;
        while(i <= index1-1) {
        	pathNew[indexOfPathNew] = path[i];
            indexOfPathNew++;
            i++;
        }
        i=index2;
        while(i>=index1){
        	pathNew[indexOfPathNew] = path[i];
        	indexOfPathNew++;
        	i--;
        }
        i=index2+1;
        while(i<path.length){
        	pathNew[indexOfPathNew] = path[i];
        	indexOfPathNew++;
        	i++;
        }
        
    	return tool.computeCost(pathNew, distanceMatrix);
    }
        
    public int[] getPath() {
    	return path;
    }
                                                  
}
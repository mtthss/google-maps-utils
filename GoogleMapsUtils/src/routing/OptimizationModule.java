package routing;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class OptimizationModule {
	
	public final static double EARTH_RADIUS_IN_METERS = 6371000; 
	
	public static List<LatLng> localRouting(List<LatLng> unsortedPoi){
		
		// declaration
        int[] path;
        int[][] distanceMatrix = new int[unsortedPoi.size()][unsortedPoi.size()];
        
        // compute relative distances
        for(int i=0; i<unsortedPoi.size();i++){
        	for(int j=0;j<unsortedPoi.size();j++){
        		distanceMatrix[i][j]=(int)distanceInMeters(unsortedPoi.get(i).latitude,unsortedPoi.get(i).longitude,unsortedPoi.get(j).latitude,unsortedPoi.get(j).longitude);
        	}
        }
        
        // first approximation
        path = NearestNeighbor.executeNearestNeighbor(distanceMatrix, 0);
        // refinement
        TwoOpt twoOpt = new TwoOpt(path, distanceMatrix);
        path = twoOpt.getPath();
        
        // returned sorted list of POIs
        List<LatLng> sortedPoi = new ArrayList<LatLng>();
        for(int i=0;i<path.length;i++){
        	sortedPoi.add(unsortedPoi.get(path[i]));
        }
        return sortedPoi;
	}
	
	public static double distanceInMeters(double lat1, double lng1, double lat2, double lng2) {
		double dLat = Math.toRadians(lat2-lat1);
		double dLng = Math.toRadians(lng2-lng1);
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		       Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
		       Math.sin(dLng/2) * Math.sin(dLng/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		return EARTH_RADIUS_IN_METERS * c;  
	}
	
}

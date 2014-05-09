package model;

import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.maps.model.LatLng;

public class OptimizationModule {
	
	public final static double EARTH_RADIUS_IN_METERS = 6371000; 
	
	public static List<LatLng> localRouting(List<LatLng> unsortedPoi){
		
		List<LatLng> sortedPoi = new ArrayList<LatLng>();
		/* traveller salesman solver targeted to small instances */
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

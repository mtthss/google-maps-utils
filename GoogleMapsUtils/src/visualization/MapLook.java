package visualization;

import java.util.List;

import model.Colour;
import model.Direction;
import model.Legs;
import model.Point;
import model.Route;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapLook {
	

	public static void drawDirection(Direction direction, GoogleMap map) {
		
		PolylineOptions polyline = null;
		
		for (Legs legs : direction.getLegsList()) {
			for (Route route : legs.getRoutesList()) {
				polyline = new PolylineOptions();
				for (Point point : route.getRoute()) {
					polyline.add(point.getLatLng());
				}
				
				setPolyLook(polyline);
				map.addPolyline(polyline);
			}
		}
	}
	
	public static void drawDirection(List<Direction> directions, GoogleMap map){
	      for(Direction direction:directions) {
	          Log.e("MainActivity", "onDirectionLoaded : Draw GDirections Called with path " + directions);
	          MapLook.drawDirection(direction, map);
	      }
	}
	
	public static void setPolyLook(PolylineOptions plo){
		plo.width(5); 
		plo.color(Color.GREEN);
	}
	
	public static void drawAreas(List<PolygonOptions> polygons, GoogleMap map){
		int i=0;
		for(PolygonOptions pol: polygons){
			i++;
			pol.add(new LatLng(45.469121, 9.178074), new LatLng(45.461318, 9.173590), new LatLng(45.456291, 9.186035));
			pol.fillColor(Color.BLUE);
			map.addPolygon(pol);
		}
		
	}
	

	
	
	
}

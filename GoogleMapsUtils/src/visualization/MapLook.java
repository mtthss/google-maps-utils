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
	
	public static void drawAreas(List<Polygon> polygons, GoogleMap map){
		int i=0;
		for(Polygon pol: polygons){
			i++;
			PolygonOptions polygon=new PolygonOptions();
			for(LatLng latlng: pol.getPoints()){
				polygon.add(latlng);
			}
			polygon.fillColor(Color.parseColor(Colour.getColor(i)));
			map.addPolygon(polygon);
		}
		
	}
	

	
	
	
}

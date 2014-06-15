package visualization;

import java.util.ArrayList;
import java.util.List;

import model.Colour;
import model.Direction;
import model.Legs;
import model.Point;
import model.Route;
import android.graphics.Color;
import android.text.GetChars;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
			pol.fillColor(getColor(i));
			pol.strokeWidth(0);
			map.addPolygon(pol);
			i++;
		}
		
	}
	
	private static int getColor(int i){
		switch (i) {
		case 0:
			return Color.argb(0, 255, 255, 102);
		case 1:
			return Color.argb(0, 178, 255, 102);
		case 2:
			return Color.argb(0, 153, 255, 255);
		case 3:
			return Color.argb(0, 255, 153, 153);
		case 4:
			return Color.argb(0, 255, 153, 51);
		case 5:
			return Color.argb(0, 51, 51, 153);
		default:
			return Color.argb(0, 255, 0, 0);
		}
	}
	public static void drawPOI(List<MarkerOptions> pois, GoogleMap map){
		for(MarkerOptions marker:pois){
			map.addMarker(marker);
		}
		
	}
	
	
	
}
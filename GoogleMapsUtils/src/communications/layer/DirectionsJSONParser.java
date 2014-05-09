package communications.layer;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import model.Legs;
import model.Route;
import model.Point;
import model.Direction;
import com.google.android.gms.maps.model.LatLng;


public class DirectionsJSONParser {

	public List<Direction> parse(JSONObject jObject) {
		
		List<Direction> directionsList = new ArrayList<Direction>();
		Direction currentDirection = null;
		List<Legs> legs = new ArrayList<Legs>();	
		Legs currentLeg = null;
		List<Route> routes = new ArrayList<Route>();
		Route currentRoute = null;
		
		JSONArray jRoutes = null;
		JSONObject jRoute;
		JSONObject jBound;
		JSONArray jLegs = null;
		JSONObject jLeg;
		JSONArray jSteps = null;
		JSONObject jStep;
		String polyline = "";
		try {
			jRoutes = jObject.getJSONArray("routes");
			Log.v("DirectionsJSONParser", "routes found : " + jRoutes.length());
			/** Traversing all routes */
			for (int i = 0; i < jRoutes.length(); i++) {
				jRoute=(JSONObject) jRoutes.get(i);
				jLegs = jRoute.getJSONArray("legs");
				Log.v("DirectionsJSONParser", "routes[" + i + "]contains jLegs found : " + jLegs.length());
				/** Traversing all legs */
				for (int j = 0; j < jLegs.length(); j++) {
					jLeg=(JSONObject) jLegs.get(j);
					jSteps = jLeg.getJSONArray("steps");
					Log.v("DirectionsJSONParser", "routes[" + i + "]:legs[" + j + "] contains jSteps found : " + jSteps.length());
					/** Traversing all steps */
					for (int k = 0; k < jSteps.length(); k++) {
						jStep = (JSONObject) jSteps.get(k);
						polyline = (String) ((JSONObject) (jStep).get("polyline")).get("points");
						// Build the List of GDPoint that define the path
						List<Point> list = decodePoly(polyline);
						// Create the GDPath
						currentRoute = new Route(list);
						currentRoute.setDistance(((JSONObject)jStep.get("distance")).getInt("value"));
						currentRoute.setDuration(((JSONObject)jStep.get("duration")).getInt("value"));
						currentRoute.setHtmlText(jStep.getString("html_instructions"));
						currentRoute.setTravelMode(jStep.getString("travel_mode"));
						Log.v("DirectionsJSONParser",
								"routes[" + i + "]:legs[" + j + "]:Step[" + k + "] contains Points found : "
										+ list.size());
						// Add it to the list of Path of the Direction
						routes.add(currentRoute);
					}
					// 
					currentLeg=new Legs(routes);
					currentLeg.setmDistance(((JSONObject)jLeg.get("distance")).getInt("value"));
					currentLeg.setmDuration(((JSONObject)jLeg.get("duration")).getInt("value"));
					currentLeg.setmEndAddress(jLeg.getString("end_address"));
					currentLeg.setmStartAddress(jLeg.getString("start_address"));
					legs.add(currentLeg);
					
					Log.v("DirectionsJSONParser", "Added a new Path and paths size is : " + routes.size());
				}
				// Build the GDirection using the paths found
				currentDirection = new Direction(legs);
				jBound=(JSONObject)jRoute.get("bounds");
				currentDirection.setmNorthEastBound(new LatLng(
						((JSONObject)jBound.get("northeast")).getDouble("lat"),
						((JSONObject)jBound.get("northeast")).getDouble("lng")));
				currentDirection.setmSouthWestBound(new LatLng(
						((JSONObject)jBound.get("southwest")).getDouble("lat"),
						((JSONObject)jBound.get("southwest")).getDouble("lng")));
				currentDirection.setCopyrights(jRoute.getString("copyrights"));
				directionsList.add(currentDirection);
			}

		} catch (JSONException e) {
			Log.e("DirectionsJSONParser", "Parsing JSon from GoogleDirection Api failed, see stack trace below:", e);
		} catch (Exception e) {
			Log.e("DirectionsJSONParser", "Parsing JSon from GoogleDirection Api failed, see stack trace below:", e);
		}
		return directionsList;
	}

	/**
	 * Method to decode polyline points
	 * Courtesy :
	 * http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
	 */
	private List<Point> decodePoly(String encoded) {

		List<Point> poly = new ArrayList<Point>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;
			poly.add(new Point((double) lat / 1E5, (double) lng / 1E5));
		}

		return poly;
	}
}
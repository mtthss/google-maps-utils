package communications;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import routing.OptimizationModule;
import android.os.AsyncTask;
import android.util.Log;
import model.Direction;

import com.google.android.gms.maps.model.LatLng;


public class GoogleMapsUtils {
	
	public final static int GEOMETRY_POSITION_JSON = 2;
	public final static int GOOGLE_API_SUPPORTED_WAYPOINTS = 0;
	public final static String MODE_DRIVING = "driving";
	public final static String MODE_WALKING = "walking";
	public final static String MODE_BICYCLING = "bicycling";
	public final static String MODE_TRANSIT = "transit";
	
	
	public static void getGeoCode(GeoCodeCallBack callback, String s) {
		
		GoogleGeocodingAsyncRestCall asyncRest = new GoogleGeocodingAsyncRestCall(callback);
		asyncRest.execute(s);
		
	}
	
	private static String getJSONGeoCode(String s){
		
		// The url for the http request
		String url="";
		try {
			url = "https://maps.googleapis.com/maps/api/geocode/json?address="+URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// The response body
		String responseBody = null;
		// The HTTP get method send to the URL
		HttpGet getMethod = new HttpGet(url);
		// The basic response handler
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		// Instantiate the HTTP communication
		HttpClient client = new DefaultHttpClient();
		// Call the URL and get the response body
		try {
			responseBody = client.execute(getMethod, responseHandler);
		} catch (ClientProtocolException e) {
			Log.e("GoogleMapsUtils", e.getMessage());
		} catch (IOException e) {
			Log.e("GoogleMapsUtils", e.getMessage());
		}
		if (responseBody != null) {
			Log.e("GoogleMapsUtils", responseBody);
		}
		// Parse the response body
		return responseBody;
	}
	
	public static void getDirection(CallBack callback, List<LatLng> unsortedCoord, String mode) {
		
		if(unsortedCoord.size()<GOOGLE_API_SUPPORTED_WAYPOINTS+2){
			GoogleDirectionAsyncRestCall asyncRest = new GoogleDirectionAsyncRestCall(callback, mode, true);
			asyncRest.execute(unsortedCoord);
		}
		else{
			List<LatLng> sortedCoord = OptimizationModule.localRouting(unsortedCoord);
			callback.setOrderedList(sortedCoord);
			if(sortedCoord.size()<=10){
				GoogleDirectionAsyncRestCall asyncRest = new GoogleDirectionAsyncRestCall(callback, mode, false);
				asyncRest.execute(sortedCoord);
			}else if(sortedCoord.size()<=19){
				int i,j;				
				List<LatLng> firstCoord = new ArrayList<LatLng>();
				List<LatLng> lastCoord = new ArrayList<LatLng>();
				for(i=0;i<10;i++){
					firstCoord.add(i, sortedCoord.get(i));
				}
				for(j=i-1;j<sortedCoord.size();j++){
					LatLng latLng = sortedCoord.get(j);
					lastCoord.add(j-9, latLng);
				}
				GoogleDirectionAsyncRestCall asyncRest1 = new GoogleDirectionAsyncRestCall(callback, mode, false);
				asyncRest1.execute(firstCoord);
				GoogleDirectionAsyncRestCall asyncRest2 = new GoogleDirectionAsyncRestCall(callback, mode, false);
				asyncRest2.execute(lastCoord);
			}else if(sortedCoord.size()>=20 && sortedCoord.size()<=28){
				int i,j;				
				List<LatLng> firstCoord = new ArrayList<LatLng>();
				List<LatLng> mediumCoord = new ArrayList<LatLng>();
				List<LatLng> lastCoord = new ArrayList<LatLng>();
				for(i=0;i<10;i++){
					firstCoord.add(i, sortedCoord.get(i));
				}
				for(j=i-1;j<19&&j<sortedCoord.size();j++){
					LatLng latLng = sortedCoord.get(j);
					mediumCoord.add(j-9, latLng);
				}
				for(j=i-1;j<sortedCoord.size();j++){
					LatLng latLng = sortedCoord.get(j);
					lastCoord.add(j-18, latLng);
				}
				GoogleDirectionAsyncRestCall asyncRest1 = new GoogleDirectionAsyncRestCall(callback, mode, false);
				asyncRest1.execute(firstCoord);
				GoogleDirectionAsyncRestCall asyncRest2 = new GoogleDirectionAsyncRestCall(callback, mode, false);
				asyncRest2.execute(mediumCoord);
				GoogleDirectionAsyncRestCall asyncRest3 = new GoogleDirectionAsyncRestCall(callback, mode, false);
				asyncRest2.execute(lastCoord);
			}
		}
	}
		
	private static String getJSONDirection(List<LatLng> listPoi, String mode, boolean remoteOptimization) {
		
		String url1 = null;
		String url = null;
		String origin = null;
		String destination = null;
		String temp = null;
		String pipe = null;
		String wayPoints = "";
		boolean wayPointsPresent = false;
		try {
			pipe = URLEncoder.encode("|","UTF-8");
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		
		origin = listPoi.get(0).latitude + "," + listPoi.get(0).longitude;
		destination = listPoi.get(listPoi.size()-1).latitude + "," + listPoi.get(listPoi.size()-1).longitude;
		
		if(listPoi.size()>2){
			wayPointsPresent = true;
			for(int i=1; i<listPoi.size()-1; i++){
				temp = listPoi.get(i).latitude + "," + listPoi.get(i).longitude;
				wayPoints = wayPoints + "via:" + temp;
				if(i+1<listPoi.size()-1){
					wayPoints = wayPoints + pipe;
				}
			}
		}
		
		if(wayPointsPresent){
			if(remoteOptimization){
				url = "http://maps.googleapis.com/maps/api/directions/json?origin="+origin+"&destination="+destination+"&mode=walking&waypoints=optimize:true" + pipe +wayPoints;
			}
			else{
				url = "http://maps.googleapis.com/maps/api/directions/json?origin="+origin+"&destination="+destination+"&mode=walking&waypoints="+wayPoints;
			}
		}else{
			url = "http://maps.googleapis.com/maps/api/directions/json?origin="+origin+"&destination="+destination+"&mode=walking";
		}
		
		// The response body
		String responseBody = null;
		// The HTTP get method send to the URL
		HttpGet getMethod = new HttpGet(url);
		// The basic response handler
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		// Instantiate the HTTP communication
		HttpClient client = new DefaultHttpClient();
		// Call the URL and get the response body
		try {
			responseBody = client.execute(getMethod, responseHandler);
		} catch (ClientProtocolException e) {
			Log.e("GoogleMapsUtils", e.getMessage());
		} catch (IOException e) {
			Log.e("GoogleMapsUtils", e.getMessage());
		}
		if (responseBody != null) {
			Log.e("GoogleMapsUtils", responseBody);
		}
		// Parse the response body
		return responseBody;
	}

	private static List<Direction> parseJsonGDir(String json) {
		
		JSONObject jObject;
		List<Direction> directions = null;
		if (json != null) {
			try {
				jObject = new JSONObject(json);
				DirectionsJSONParser parser = new DirectionsJSONParser();
				directions = parser.parse(jObject);
			} catch (Exception e) {
				Log.e("GoogleMapsUtils", "Parsing JSon from GoogleDirection Api failed, see stack trace below:", e);
			}
		} else {
			directions = new ArrayList<Direction>();
		}
		return directions;
	}
	
	private static LatLng parseJsonGeoCode(String json){

		JSONObject jObject;
		LatLng encodedAddress = null;
		if (json != null) {
			try {
				jObject = new JSONObject(json);
				JSONArray resultsArray = jObject.getJSONArray("results");
				JSONObject resultsObj = resultsArray.getJSONObject(0);
				JSONObject geometry = resultsObj.getJSONObject("geometry");
				JSONObject location = geometry.getJSONObject("location");
				double lat = Double.parseDouble(location.getString("lat"));
				double lng = Double.parseDouble(location.getString("lng"));
				encodedAddress = new LatLng(lat,lng);
			} catch (Exception e) {
				Log.e("GoogleMapsUtils", "Parsing JSon from GeoCoding Api failed, see stack trace below:", e);
			}
		} else {
			encodedAddress = null;
		}
		return encodedAddress;
	}
	
	// internal class for requesting google directions
	public static final class GoogleDirectionAsyncRestCall extends AsyncTask<List<LatLng>, Void, List<Direction>> {

		private String mDirectionMode = null;
		private CallBack callback;
		private boolean remoteOptimization;
		private List<LatLng> ordered;
		
		
		public GoogleDirectionAsyncRestCall(CallBack callback, String mDirectionMode, boolean remote) {
			super();
			
			this.mDirectionMode = mDirectionMode;
			this.callback = callback;
			this.remoteOptimization = remote;
		}

		@Override
		protected List<Direction> doInBackground(List<LatLng>... arg0) {
			
			ordered = arg0[0];
			// Do the rest http call
			String json = getJSONDirection(arg0[0], mDirectionMode, remoteOptimization);
			// Parse the element and return it
			return parseJsonGDir(json);
		}
		
		@Override
		protected void onPostExecute(List<Direction> result) {
			
			super.onPostExecute(result);
			// Just pass the result to the callback
			callback.onDirectionLoaded(result, ordered);
		}
	}
	
	// Internal class for google Geocoding
	public static final class GoogleGeocodingAsyncRestCall extends AsyncTask<String, Void, LatLng> {

		private GeoCodeCallBack callback;		
		
		public GoogleGeocodingAsyncRestCall(GeoCodeCallBack callback) {
			super();
			this.callback = callback;
		}

		@Override
		protected LatLng doInBackground(String... arg0) {
			
			String json = getJSONGeoCode(arg0[0]);
			return parseJsonGeoCode(json);
		}
		
		@Override
		protected void onPostExecute(LatLng result) {
			
			super.onPostExecute(result);
			callback.onGeoCodeComputed(result);
		}
	}
	
	
}

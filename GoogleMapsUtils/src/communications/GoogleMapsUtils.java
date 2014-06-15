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
import org.json.JSONObject;

import routing.OptimizationModule;
import android.os.AsyncTask;
import android.util.Log;
import model.Direction;

import com.google.android.gms.maps.model.LatLng;

public class GoogleMapsUtils {
	
	public final static int GOOGLE_API_SUPPORTED_WAYPOINTS = 8;
	public final static String MODE_DRIVING = "driving";
	public final static String MODE_WALKING = "walking";
	public final static String MODE_BICYCLING = "bicycling";
	public final static String MODE_TRANSIT = "transit";
	
	
	public static LatLng getGeoCode(String s) {
		
		//TODO MATTEO implementa metodo che usa il servizio di geocoding di google
		return new LatLng(45.4627338,9.1777322);
	}
	
	public static void getDirection(CallBack callback, List<LatLng> unsortedPoi, String mode) {
		
		if(unsortedPoi.size()<GOOGLE_API_SUPPORTED_WAYPOINTS){
			GoogleDirectionAsyncRestCall asyncRest= new GoogleDirectionAsyncRestCall(callback, mode, true);
			asyncRest.execute(unsortedPoi);
		}
		else{
			//List<LatLng> sortedPoi = OptimizationModule.localRouting(unsortedPoi);
			GoogleDirectionAsyncRestCall asyncRest= new GoogleDirectionAsyncRestCall(callback, mode, false);
			//asyncRest.execute(sortedPoi);
			asyncRest.execute(unsortedPoi);
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
	
	/**
	 * This class aims to make an async call to the server and retrieve the Json representing the Direction
	 * Then build the GDirection object
	 * Then post it to the DCACallBack in the UI Thread
	 */
	public static final class GoogleDirectionAsyncRestCall extends AsyncTask<List<LatLng>, Void, List<Direction>> {

		private String mDirectionMode = null;
		private CallBack callback;
		private boolean remoteOptimization;
		
		
		public GoogleDirectionAsyncRestCall(CallBack callback, String mDirectionMode, boolean remote) {
			super();
			
			this.mDirectionMode = mDirectionMode;
			this.callback = callback;
			this.remoteOptimization = remote;
		}

		@Override
		protected List<Direction> doInBackground(List<LatLng>... arg0) {
			
			// Do the rest http call
			String json = getJSONDirection(arg0[0], mDirectionMode, remoteOptimization);
			// Parse the element and return it
			return parseJsonGDir(json);
		}
		
		@Override
		protected void onPostExecute(List<Direction> result) {
			
			super.onPostExecute(result);
			// Just pass the result to the callback
			callback.onDirectionLoaded(result);
		}
	}
	
}

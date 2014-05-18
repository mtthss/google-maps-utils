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
	
	public final static int GOOGLE_API_SUPPORTED_WAYPOINTS = 10;
	public final static String MODE_DRIVING = "driving";
	public final static String MODE_WALKING = "walking";
	public final static String MODE_BICYCLING = "bicycling";
	public final static String MODE_TRANSIT = "transit";
	
	
	public static void getDirection(CallBack callback, List<LatLng> unsortedPoi, String mode) {
		
		if(unsortedPoi.size()<GOOGLE_API_SUPPORTED_WAYPOINTS){
			GoogleDirectionAsyncRestCall asyncRest= new GoogleDirectionAsyncRestCall(callback, mode, true);
			asyncRest.execute(unsortedPoi);
		}
		else{
			List<LatLng> sortedPoi = OptimizationModule.localRouting(unsortedPoi);
			GoogleDirectionAsyncRestCall asyncRest= new GoogleDirectionAsyncRestCall(callback, mode, false);
			asyncRest.execute(sortedPoi);
		}
	}
	
	private static String getJSONDirection(List<LatLng> listPoi, String mode, boolean remoteOptimization) {
		// SE NON FUNZIA PROVARE AD AGGIUNGERE IN CODA << +"&sensor=false&key=API_KEY" >>
		LatLng start = listPoi.get(0);
		LatLng end = listPoi.get(listPoi.size()-1);
		
		String url=null;
		try {
			url = URLEncoder.encode("http://maps.googleapis.com/maps/api/directions/json?origin=45.2,9.4&destination=45.5,9.6&sensor=false&units=metric&mode=walking&waypoints=45.4,10|45.2,9.31", "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		// initialize url with start point and end point
		String turl = "http://maps.googleapis.com/maps/api/directions/json?" + "origin=" + start.latitude + ","
				+ start.longitude + "&destination=" + end.latitude + "," + end.longitude
				+ "&sensor=false&units=metric&mode=" + mode + "&waypoints=optimize:";
		
		// add way points and choose optimization policy
		if(listPoi.size()>2){
			
			// choose optimization policy
			if(remoteOptimization){
				turl = turl + "true";
			}else{
				turl = turl + "false";
			}
			
			// add way points
			for(int i=1;i<listPoi.size()-1;i++){
				turl = turl + "|" + listPoi.get(i).latitude + "," + listPoi.get(i).longitude; 
			}
		}
		
		String responseBody = null;
		// The HTTP get method send to the URL
		HttpGet getMethod = new HttpGet(url);
		// The basic response handler
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		// instantiate the http communication
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
		// parse the response body
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
		
		/*
		protected void onProgressUpdate(Integer... progress) {
	         setProgressPercent(progress[0]);
	     }
		*/
		
		@Override
		protected void onPostExecute(List<Direction> result) {
			super.onPostExecute(result);
			// Just pass the result to the callback
			callback.onDirectionLoaded(result);
		}
	}
	
}

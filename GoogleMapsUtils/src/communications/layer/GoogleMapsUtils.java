package communications.layer;

import java.io.IOException;
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
	
	public final static int GOOGLE_API_SUPPORTED_WAYPOINTS = 0;
	public final static String MODE_DRIVING = "driving";
	public final static String MODE_WALKING = "walking";
	public final static String MODE_BICYCLING = "bicycling";
	public final static String MODE_TRANSIT = "transit";
	
	public static void getDirection(CallBack callback, LatLng start, LatLng end, String mode) {
		GoogleDirectionAsyncRestCall async = new GoogleDirectionAsyncRestCall(callback, mode);
		async.execute(start, end);
	}
	
	public static void getDirection(CallBack callback, List<LatLng> listPoi, String mode) {
		
		if(listPoi.size()<GOOGLE_API_SUPPORTED_WAYPOINTS){
			GoogleDirectionAsyncRestCall asyncRest= new GoogleDirectionAsyncRestCall(callback, mode);
			//asyncRest.execute(listPoi);
		}
		else{
			OptimizationModule.localRouting(listPoi);
			GoogleDirectionAsyncRestCall asyncRest= new GoogleDirectionAsyncRestCall(callback, mode);
			//asyncRest.execute(listPoi);
		}
	}
	
	private static String getJSONDirection(LatLng start, LatLng end, String mode) {
		
		LatLng intermediate1 = new LatLng(45.45,9.21);
		LatLng intermediate2 = new LatLng(45.42,9.23);
		
		// SE NON FUNZIA PROVARE AD AGGIUNGERE IN CODA << +"&sensor=false&key=API_KEY" >>
		String url = "http://maps.googleapis.com/maps/api/directions/json?" + "origin=" + start.latitude + ","
				+ start.longitude + "&destination=" + end.latitude + "," + end.longitude
				+ "&sensor=false&units=metric&mode=" + mode
				+ "&waypoints=optimize:true|" + intermediate1.latitude + "," + intermediate1.longitude 
				+ "|" + intermediate2.latitude + "," + intermediate2.longitude;
		
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
	

	////////////////////////////////////////////////////////////////////
	//	INTERNAL CLASS -> PROVARE A SPOSTARLA FUORI TANTO E' STATICA  //
	////////////////////////////////////////////////////////////////////
	/**
	 * This class aims to make an async call to the server and retrieve the Json representing
	 * the Direction
	 * Then build the GDirection object
	 * Then post it to the DCACallBack in the UI Thread
	 */
	public static final class GoogleDirectionAsyncRestCall extends AsyncTask<LatLng, String, List<Direction>> {

		private String mDirectionMode = null;
		private CallBack callback;


		public GoogleDirectionAsyncRestCall(CallBack callback, String mDirectionMode) {
			super();
			this.mDirectionMode = mDirectionMode;
			this.callback = callback;
		}

		@Override
		protected List<Direction> doInBackground(LatLng... arg0) {
			// Do the rest http call
			String json = getJSONDirection(arg0[0], arg0[1], mDirectionMode);
			// Parse the element and return it
			return parseJsonGDir(json);
		}
		
		@Override
		protected void onPostExecute(List<Direction> result) {
			super.onPostExecute(result);
			// Just call the callback
			callback.onDirectionLoaded(result);
		}
	}
	////////////////////////////////////////////////////////////////////
	//	END INTERNAL CLASS							                  //
    ////////////////////////////////////////////////////////////////////
	
}

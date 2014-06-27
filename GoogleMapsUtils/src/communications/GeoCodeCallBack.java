package communications;

import com.google.android.gms.maps.model.LatLng;

public interface GeoCodeCallBack {

	public void onGeoCodeComputed(LatLng result);
}
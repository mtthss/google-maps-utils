
package communications;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import model.Direction;


public interface CallBack {

	public void onDirectionLoaded(List<Direction> directions, List<LatLng> ordered);
	
	public void setOrderedList(List<LatLng> orderedPois);
}

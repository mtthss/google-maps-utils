
package model;

import java.util.List;

/**
 * This class aims toThis class aims to define a GoogleDirection Path which is bound to the JSon structure
 *        returned by the webService :
 *        "http://maps.googleapis.com/maps/api/directions/json?" + "origin=" + start.latitude + ","
 *        + start.longitude + "&destination=" + end.latitude + "," + end.longitude
 *        + "&sensor=false&units=metric&mode=driving";
 */
public class Route {

	List<Point> mRoute;
	int mDistance;
	int mDuration;
	String mTravelMode;
	String mHtmlText;

	public Route(List<Point> route) {
		super();
		this.mRoute = route;
	}

	public final List<Point> getRoute() {
		return mRoute;
	}

	public final void setRoute(List<Point> mRoute) {
		this.mRoute = mRoute;
	}

	public final List<Point> getmRoute() {
		return mRoute;
	}

	public final int getDistance() {
		return mDistance;
	}

	public final int getDuration() {
		return mDuration;
	}

	public final String getTravelMode() {
		return mTravelMode;
	}

	public final String getHtmlText() {
		return mHtmlText;
	}

	public final void setmPath(List<Point> mPath) {
		this.mRoute = mPath;
	}

	public final void setDistance(int distance) {
		this.mDistance = distance;
	}

	public final void setDuration(int duration) {
		this.mDuration = duration;
	}

	public final void setTravelMode(String travelMode) {
		this.mTravelMode = travelMode;
	}

	public final void setHtmlText(String htmlText) {
		this.mHtmlText = htmlText;
	}

	@Override
	public String toString() {
		StringBuilder strB=new StringBuilder("GPath\r\n");
		for(Point point:mRoute) {
			strB.append(point.toString());
			strB.append(point.toString());
			strB.append(",");
		}
		return strB.toString();
	}

}

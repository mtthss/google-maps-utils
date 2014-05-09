/**<ul>
 * <li>GoogleMapSample</li>
 * <li>com.android2ee.formation.librairies.google.map.utils.direction.model</li>
 * <li>13 sept. 2013</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : Mathias Seguy Project</li>
 * <li>Produit par MSE.</li>
 *
 /**
 * <ul>
 * Android Tutorial, An <strong>Android2EE</strong>'s project.</br> 
 * Produced by <strong>Dr. Mathias SEGUY</strong>.</br>
 * Delivered by <strong>http://android2ee.com/</strong></br>
 *  Belongs to <strong>Mathias Seguy</strong></br>
 ****************************************************************************************************************</br>
 * This code is free for any usage except training and can't be distribute.</br>
 * The distribution is reserved to the site <strong>http://android2ee.com</strong>.</br>
 * The intelectual property belongs to <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * 
 * *****************************************************************************************************************</br>
 *  Ce code est libre de toute utilisation mais n'est pas distribuable.</br>
 *  Sa distribution est reservée au site <strong>http://android2ee.com</strong>.</br> 
 *  Sa propriété intellectuelle appartient à <strong>Mathias Seguy</strong>.</br>
 *  <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * *****************************************************************************************************************</br>
 */
package model;

import java.util.List;


public class Legs {

	List<Route> mRoutesList;

	int mDistance;
	int mDuration;
	String mStartAddress;
	String mEndAddress;

	public Legs(List<Route> routesList) {
		super();
		this.mRoutesList = routesList;
	}
	public final List<Route> getRoutesList() {
		return mRoutesList;
	}

	public final void setRoutesList(List<Route> mPathsList) {
		this.mRoutesList = mPathsList;
	}

	public final int getmDistance() {
		return mDistance;
	}

	public final void setmDistance(int mDistance) {
		this.mDistance = mDistance;
	}

	public final int getmDuration() {
		return mDuration;
	}

	public final void setmDuration(int mDuration) {
		this.mDuration = mDuration;
	}

	public final String getmStartAddress() {
		return mStartAddress;
	}

	public final void setmStartAddress(String mStartAddress) {
		this.mStartAddress = mStartAddress;
	}

	public final String getmEndAddress() {
		return mEndAddress;
	}

	public final void setmEndAddress(String mEndAddress) {
		this.mEndAddress = mEndAddress;
	}

	@Override
	public String toString() {
		StringBuilder strB=new StringBuilder("GLegs\r\n");
		for(Route path:mRoutesList) {
			strB.append(path.toString());
			strB.append("\r\n");
		}
		return strB.toString();
	}
}

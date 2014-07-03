/**<ul>
 * <li>GoogleMapSample</li>
 * <li>com.android2ee.formation.librairies.google.map.utils.direction</li>
 * <li>12 sept. 2013</li>
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

import com.google.android.gms.maps.model.LatLng;

public class Direction {

	List<Legs> mLegsList;

	LatLng mNorthEastBound;
	
	LatLng mSouthWestBound;
	
	String copyrights;

	public Direction(List<Legs> legsList) {
		super();
		this.mLegsList = legsList;
	}

	public final List<Legs> getLegsList() {
		return mLegsList;
	}

	public final void setPathsList(List<Legs> mLegsList) {
		this.mLegsList = mLegsList;
	}

	public final LatLng getmNorthEastBound() {
		return mNorthEastBound;
	}

	public final void setmNorthEastBound(LatLng mNorthEastBound) {
		this.mNorthEastBound = mNorthEastBound;
	}

	public final LatLng getmSouthWestBound() {
		return mSouthWestBound;
	}

	public final void setmSouthWestBound(LatLng mSouthWestBound) {
		this.mSouthWestBound = mSouthWestBound;
	}
	
	public final String getCopyrights() {
		return copyrights;
	}

	public final void setCopyrights(String copyrights) {
		this.copyrights = copyrights;
	}
	
	@Override
	public String toString() {
		StringBuilder strB = new StringBuilder("GDirection\r\n");
		for (Legs path : mLegsList) {
			strB.append(path.toString());
			strB.append("\r\n");
		}
		return strB.toString();
	}

}

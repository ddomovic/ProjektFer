/**
 *
 */
package fer.projekt.neuralnetwork.regression.dataset1;

/**
 * @author Nikola
 *
 */
public class Data {
	private int OSM_ID;
	private double longitude;
	private double latitude;
	private double altitude;

	public Data(int oSM_ID, double longitude, double latitude, double altitude) {
		super();
		OSM_ID = oSM_ID;
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
	}
	/**
	 * @return the oSM_ID
	 */
	public int getOSM_ID() {
		return OSM_ID;
	}
	/**
	 * @param oSM_ID the oSM_ID to set
	 */
	public void setOSM_ID(int oSM_ID) {
		OSM_ID = oSM_ID;
	}
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the altitude
	 */
	public double getAltitude() {
		return altitude;
	}
	/**
	 * @param altitude the altitude to set
	 */
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
}

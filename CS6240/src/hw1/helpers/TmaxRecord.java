package hw1.helpers;

/*
 * An object which saves the station and the reading for TMAX of that station.
 */
public class TmaxRecord  {
	private String station;
	private double tmaxReading;
	
	public TmaxRecord(String station, double reading) {
		this.station = station;
		this.tmaxReading = reading;
	}
	
	public String getStation() {
		return station;
	}
	public double getReading() {
		return tmaxReading;
	}
}
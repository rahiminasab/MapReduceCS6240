package hw1.misc;

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
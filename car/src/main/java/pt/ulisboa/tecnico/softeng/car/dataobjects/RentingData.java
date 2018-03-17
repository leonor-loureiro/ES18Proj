package pt.ulisboa.tecnico.softeng.car.dataobjects;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.domain.Renting;

public class RentingData {
	private String reference; //static?
	private String plate;
	private String drivingLicense;
	private String rentACarCode;
	private LocalDate begin;
	private LocalDate end;
	
	public RentingData(Renting renting) {
		this.reference      = renting.getReference();
		this.plate          = renting.getPlate();
		this.drivingLicense = renting.getDrivingLicense();
		this.rentACarCode   = renting.getRentACarCode();
		this.begin          = renting.getBegin();
		this.end            = renting.getEnd();
	}
	
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getPlate() {
		return plate;
	}
	public void setPlate(String plate) {
		this.plate = plate;
	}
	public String getDrivingLicense() {
		return drivingLicense;
	}
	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}
	public String getRentACarCode() {
		return rentACarCode;
	}
	public void setRentACarCode(String rentACarCode) {
		this.rentACarCode = rentACarCode;
	}
	public LocalDate getBegin() {
		return begin;
	}
	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}
	public LocalDate getEnd() {
		return end;
	}
	public void setEnd(LocalDate end) {
		this.end = end;
	}
	
}

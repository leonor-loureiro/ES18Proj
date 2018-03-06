package pt.ulisboa.tecnico.softeng.car;

import org.joda.time.LocalDate;

public class Renting {
	private String _reference;
	private String _drivingLicence;
	private LocalDate _begin;
	private LocalDate _end;
	private int _kilometers;
	
	public boolean conflict(LocalDate begin, LocalDate end) {
		return false;
	}
	
	public int checkOut(int kilometers) {
		return -1;
	}
}

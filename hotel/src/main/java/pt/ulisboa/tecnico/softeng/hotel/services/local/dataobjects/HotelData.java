package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;

public class HotelData {
	private String code;
	private String name;

	public HotelData() {
	}

	public HotelData(Hotel hotel) {
		this.code = hotel.getCode();
		this.name = hotel.getName();
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import java.util.List;
import java.util.stream.Collectors;

import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;

public class HotelData {
	private String code;
	private String name;
	private String nif;
	private String iban;
	private Double priceSingle;
	private Double priceDouble;
	private List<RoomData> rooms;

	public HotelData() {
	}

	public HotelData(Hotel hotel) {
		this.code = hotel.getCode();
		this.name = hotel.getName();

		this.rooms = hotel.getRoomSet().stream().sorted((r1, r2) -> r1.getNumber().compareTo(r2.getNumber()))
				.map(r -> new RoomData(r)).collect(Collectors.toList());

		this.nif = hotel.getNif();
		this.iban = hotel.getIban();
		this.priceSingle = hotel.getPriceSingle();
		this.priceDouble = hotel.getPriceDouble();
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

	public String getNif() {
		return this.nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public Double getPriceSingle() {
		return this.priceSingle;
	}

	public void setPriceSingle(Double priceSingle) {
		this.priceSingle = priceSingle;
	}

	public Double getPriceDouble() {
		return this.priceDouble;
	}

	public void setPriceDouble(Double priceDouble) {
		this.priceDouble = priceDouble;
	}

	public List<RoomData> getRooms() {
		return this.rooms;
	}

	public void setRooms(List<RoomData> rooms) {
		this.rooms = rooms;
	}

}

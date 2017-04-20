package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import java.util.List;
import java.util.stream.Collectors;

import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;

public class HotelData {
	private String code;
	private String name;
	private List<RoomData> rooms;

	public HotelData() {
	}

	public HotelData(Hotel hotel) {
		this.code = hotel.getCode();
		this.name = hotel.getName();

		this.rooms = hotel.getRoomSet().stream().sorted((r1, r2) -> r1.getNumber().compareTo(r2.getNumber()))
				.map(r -> new RoomData(r)).collect(Collectors.toList());
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

	public List<RoomData> getRooms() {
		return this.rooms;
	}

	public void setRooms(List<RoomData> rooms) {
		this.rooms = rooms;
	}

}

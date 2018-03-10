package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.dataobject.RentingData;

public class RentACar {
	private static int codeValue = 0;
	private String name;
	private String code;
	private ArrayList<Car> listCars;
	private ArrayList<Motorcycle> listMotorcycles;
	
	public RentACar(String name) {
		this.name = name;
	}
	
	public Renting getRenting(String reference) {
		Stream<Renting> carRentings   = this.listCars.stream().flatMap(o -> o.getRentings().stream());
		Stream<Renting> motorCRenting = this.listMotorcycles.stream().flatMap(o -> o.getRentings().stream());
		Stream<Renting> listRentings  = Stream.concat(carRentings, motorCRenting);
		
		return listRentings.filter(rent -> rent.getReference().equals(reference)).findFirst().orElse(null);
	}
	
	public List<Car> getAllAvailableCars(LocalDate begin, LocalDate end) {
		return this.listCars.stream().filter(car -> car.getRentings().stream().allMatch(rent -> !rent.conflict(begin, end))).collect(Collectors.toList());
	}
	
	public List<Motorcycle> getAllAvailableMotorcycles(LocalDate begin, LocalDate end) {
		return this.listMotorcycles.stream().filter(motorC -> motorC.getRentings().stream().allMatch(rent -> !rent.conflict(begin, end))).collect(Collectors.toList());
	}
	
	public RentingData getRentingData(String reference) {
		Renting renting = getRenting(reference);
		if (renting != null)
			return new RentingData(renting);
		return null;
	}
	
	public String getCode() {
		return "" + codeValue++;
	}
}
	

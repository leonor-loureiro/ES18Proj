package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.dataobject.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.exception.RentingException;

public class RentACar {
	private static int counter = 0;
	
	public static Set<RentACar> rentACars     = new HashSet<>();
	private final Set<Car> cars               = new HashSet<>();
	private final Set<Motorcycle> motorcycles = new HashSet<>();
	
	private String name;
	private String code;
	
	public RentACar(String name) {
		checkArguments(name);
		
		this.name = name;
		this.code = "" + Integer.toString(++counter);
		
		RentACar.rentACars.add(this);
	}
	
	private void checkDates(LocalDate begin, LocalDate end) {
		if (begin == null || end == null || begin.isAfter(end))
			throw new CarException("RentACar Exception: Invalid dates.");
	}
	
	private void checkArguments(String name) {
		if (name == null || !name.matches("^[a-zA-Z].*"))
			throw new CarException("RentACar Exception: Invalid Name \"" + name + "\".");
		if (rentACars.stream().anyMatch(rac -> rac.getName().equals(name)))
			throw new CarException("RentACar Exception: RentACar \"" + name + "\" already exists.");
	}
	
	public Renting getRenting(String reference) {
		Stream<Renting> carRentings   = this.cars.stream().map(o -> o.getRentings()).flatMap(l -> l.stream());
		Stream<Renting> motorCRenting = this.motorcycles.stream().map(o -> o.getRentings()).flatMap(l -> l.stream());
		Stream<Renting> listRentings  = Stream.concat(carRentings, motorCRenting);
		
		return listRentings.filter(rent -> rent.getReference().equals(reference)).findFirst().orElse(null);
	}
	
	public static Renting getRentingByReference(String reference) {
		return RentACar.rentACars.stream().map(rac -> rac.getRenting(reference)).filter(Objects::nonNull).findFirst().orElse(null);
	}
	
	public List<Car> getAllAvailableCars(LocalDate begin, LocalDate end) {
		checkDates(begin, end);
		return this.cars.stream().filter(car -> car.getRentings().stream().allMatch(rent -> !rent.conflict(begin, end))).collect(Collectors.toList());
	}
	
	public List<Motorcycle> getAllAvailableMotorcycles(LocalDate begin, LocalDate end) {
		checkDates(begin, end);
		return this.motorcycles.stream().filter(motorC -> motorC.getRentings().stream().allMatch(rent -> !rent.conflict(begin, end))).collect(Collectors.toList());
	}
	
	public static RentingData getRentingData(String reference) {
		Renting renting = getRentingByReference(reference);
		if (renting != null)
			return new RentingData(renting);
		throw new RentingException("RentACar Exception: No renting found.");
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getCode() {
		return this.code;
	}

	public void addVehicle(Vehicle vehicle) {
		if (vehicle instanceof Car)
			this.cars.add((Car) vehicle);
		else if (vehicle instanceof Motorcycle)
			this.motorcycles.add((Motorcycle) vehicle);
		else throw new CarException("RentACar Exception: Vehicle type not known.");
	}
}
	

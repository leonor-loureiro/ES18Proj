package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.domain.Processor;

public class RentACar {
	public static final Set<RentACar> rentACars = new HashSet<>();

	private static int counter;

	private final String name;
	private final String code;
	private final String NIF;
	private final String IBAN;
	private final Map<String, Vehicle> vehicles = new HashMap<>();
	
	private final Processor processor = new Processor();

	public RentACar(String name, String NIF, String IBAN) {
		checkArguments(name, NIF, IBAN);
		this.name = name;
		this.code = Integer.toString(++RentACar.counter);
		this.NIF = NIF;
		this.IBAN = IBAN;
		
		rentACars.add(this);
	}

	private void checkArguments(String name, String NIF, String IBAN) {
		if (name == null || name.trim().isEmpty()) {
			throw new CarException();
		}

		if (NIF == null || NIF.length() != 9) {
			throw new CarException();
		}
		
		if (IBAN == null || IBAN.length() < 5) {
			throw new CarException();
		}
	}
	
	public Processor getProcessor() {
		return this.processor;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	public String getNIF() {
		return NIF;
	}

	public String getIBAN() {
		return IBAN;
	}
	
	void addVehicle(Vehicle vehicle) {
		this.vehicles.put(vehicle.getPlate(), vehicle);
	}

	public boolean hasVehicle(String plate) {
		return vehicles.containsKey(plate);
	}

	public Set<Vehicle> getAvailableVehicles(Class<?> cls, LocalDate begin, LocalDate end) {
		Set<Vehicle> availableVehicles = new HashSet<>();
		for (Vehicle vehicle : this.vehicles.values()) {
			if (cls == vehicle.getClass() && vehicle.isFree(begin, end)) {
				availableVehicles.add(vehicle);
			}
		}
		return availableVehicles;
	}

	private static Set<Vehicle> getAllAvailableVehicles(Class<?> cls, LocalDate begin, LocalDate end) {
		Set<Vehicle> vehicles = new HashSet<>();
		for (RentACar rentACar : rentACars) {
			vehicles.addAll(rentACar.getAvailableVehicles(cls, begin, end));
		}
		return vehicles;
	}

	public static Set<Vehicle> getAllAvailableMotorcycles(LocalDate begin, LocalDate end) {
		return getAllAvailableVehicles(Motorcycle.class, begin, end);
	}

	public static Set<Vehicle> getAllAvailableCars(LocalDate begin, LocalDate end) {
		return getAllAvailableVehicles(Car.class, begin, end);
	}

	/**
	 * Lookup for a renting using its reference.
	 * 
	 * @param reference
	 * @return the renting with the given reference.
	 */
	public static Renting getRenting(String reference) {
		for (RentACar rentACar : rentACars) {
			for (Vehicle vehicle : rentACar.vehicles.values()) {
				Renting renting = vehicle.getRenting(reference);
				if (renting != null) {
					return renting;
				}
			}
		}
		return null;
	}
	
	public static String rentVehicle(Class<?> cls, String driving_licence, LocalDate begin, LocalDate end, String clientNIF, String clientIBAN) {
		Set<Vehicle> available = getAllAvailableVehicles(cls, begin, end);
		if(available.size() > 0) {
			Renting renting = available.iterator().next().rent(driving_licence, begin, end, clientNIF, clientIBAN);
			available.iterator().next().getRentACar().getProcessor().submitRenting(renting);
			return renting.getReference();	
		}
		throw new CarException();
	}

	public static RentingData getRentingData(String reference) {
		Renting renting = getRenting(reference);
		if (renting == null) {
			throw new CarException();
		}
		return new RentingData(
			renting.getReference(),
			renting.getVehicle().getPlate(),
			renting.getDrivingLicense(),
			renting.getVehicle().getRentACar().getCode(),
			renting.getBegin(),
			renting.getEnd()
		);
	}
}

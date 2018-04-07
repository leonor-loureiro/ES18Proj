package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACar {
	public static final Set<RentACar> rentACars = new HashSet<>();

	private static int counter;

	public int getNextCounter() {
		return ++counter;
	}

	private final String name;
	private final String code;
	private final String nif;
	private final String iban;
	private final Map<String, Vehicle> vehicles = new HashMap<>();

	private final Processor processor = new Processor();

	public RentACar(String name, String nif, String iban) {
		checkArguments(name, nif, iban);
		this.name = name;
		this.nif = nif;
		this.iban = iban;
		this.code = nif + Integer.toString(getNextCounter());

		rentACars.add(this);
	}

	private void checkArguments(String name, String nif, String iban) {
		if (name == null || name.isEmpty() || nif == null || nif.isEmpty() || iban == null || iban.isEmpty()) {

			throw new CarException();
		}

		for (final RentACar rental : rentACars) {
			if (rental.getNIF().equals(nif)) {
				throw new CarException();
			}
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	public String getNIF() {
		return this.nif;
	}

	public String getIBAN() {
		return this.iban;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return this.code;
	}

	void addVehicle(Vehicle vehicle) {
		this.vehicles.put(vehicle.getPlate(), vehicle);
	}

	public boolean hasVehicle(String plate) {
		return this.vehicles.containsKey(plate);
	}

	public Set<Vehicle> getAvailableVehicles(Class<?> cls, LocalDate begin, LocalDate end) {
		final Set<Vehicle> availableVehicles = new HashSet<>();
		for (final Vehicle vehicle : this.vehicles.values()) {
			if (cls == vehicle.getClass() && vehicle.isFree(begin, end)) {
				availableVehicles.add(vehicle);
			}
		}
		return availableVehicles;
	}

	private static Set<Vehicle> getAllAvailableVehicles(Class<?> cls, LocalDate begin, LocalDate end) {
		final Set<Vehicle> vehicles = new HashSet<>();
		for (final RentACar rentACar : rentACars) {
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

	public static String rent(Class<? extends Vehicle> vehicleType, String drivingLicense, String buyerNIF,
			String buyerIBAN, LocalDate begin, LocalDate end) {
		Set<Vehicle> availableVehicles;

		if (vehicleType == Car.class) {
			availableVehicles = getAllAvailableCars(begin, end);
		} else {
			availableVehicles = getAllAvailableMotorcycles(begin, end);
		}

		return availableVehicles.stream().findFirst().map(v -> v.rent(drivingLicense, begin, end, buyerNIF, buyerIBAN))
				.orElseThrow(CarException::new).getReference();
	}

	public static String cancelRenting(String reference) {
		final Renting renting = getRenting(reference);

		if (renting != null) {
			return renting.cancel();
		}

		throw new CarException();
	}

	/**
	 * Lookup for a renting using its reference.
	 *
	 * @param reference
	 * @return the renting with the given reference.
	 */
	protected static Renting getRenting(String reference) {
		for (final RentACar rentACar : rentACars) {
			for (final Vehicle vehicle : rentACar.vehicles.values()) {
				final Renting renting = vehicle.getRenting(reference);
				if (renting != null) {
					return renting;
				}
			}
		}
		return null;
	}

	public static RentingData getRentingData(String reference) {
		final Renting renting = getRenting(reference);
		if (renting == null) {
			throw new CarException();
		}
		return new RentingData(renting);
	}

	public Processor getProcessor() {
		return this.processor;
	}

}

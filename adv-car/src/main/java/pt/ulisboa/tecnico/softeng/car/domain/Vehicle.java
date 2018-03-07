package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public abstract class Vehicle {
	private static String plateFormat = "..-..-..";
	static Set<String> plates = new HashSet<String>();

	private final String plate;
	private int kilometers;
	private final RentACar rentACar;
	public final Map<String, Renting> rentings = new HashMap<>();

	public Vehicle(String plate, int kilometers, RentACar rentACar) {
		System.out.println(plate);
		checkArguments(plate, kilometers, rentACar);

		this.plate = plate;
		this.kilometers = kilometers;
		this.rentACar = rentACar;

		plates.add(plate.toUpperCase());
		rentACar.addVehicle(this);
	}

	private void checkArguments(String plate, int kilometers, RentACar rentACar) {
		if (plate == null || !plate.matches(plateFormat) || plates.contains(plate.toUpperCase())) {
			throw new CarException();
		} else if (kilometers < 0) {
			throw new CarException();
		} else if (rentACar == null) {
			throw new CarException();
		}
	}

	/**
	 * @return the plate
	 */
	public String getPlate() {
		return plate;
	}

	/**
	 * @return the kilometers
	 */
	public int getKilometers() {
		return kilometers;
	}

	/**
	 * @param kilometers
	 *            the kilometers to set
	 */
	public void addKilometers(int kilometers) {
		if (kilometers < 0) {
			throw new CarException();
		}
		this.kilometers += kilometers;
	}

	/**
	 * @return the rentACar
	 */
	public RentACar getRentACar() {
		return rentACar;
	}

	public boolean isFree(LocalDate begin, LocalDate end) {
		if (begin == null || end == null) {
			throw new CarException();
		}
		for (Renting renting : rentings.values()) {
			if (renting.conflict(begin, end)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Add a <code>Renting</code> object to the vehicle. Use with caution --- no
	 * validation is being made.
	 * 
	 * @param renting
	 */
	private void addRenting(Renting renting) {
		this.rentings.put(renting.getReference(), renting);
	}

	/**
	 * Lookup for a <code>Renting</code> with the given reference.
	 * 
	 * @param reference
	 * @return Renting with the given reference
	 */
	public Renting getRenting(String reference) {
		return this.rentings.get(reference);
	}

	/**
	 * @param drivingLicense
	 * @param begin
	 * @param end
	 * @return
	 */
	public Renting rent(String drivingLicense, LocalDate begin, LocalDate end) {
		if (!isFree(begin, end)) {
			throw new CarException();
		}

		Renting renting = new Renting(drivingLicense, begin, end, this);
		this.addRenting(renting);

		return renting;
	}
}

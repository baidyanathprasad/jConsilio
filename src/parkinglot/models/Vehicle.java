package parkinglot.models;

import parkinglot.enums.VehicleType;
import parkinglot.exceptions.InvalidArgumentException;

/**
 * Represents a vehicle.
 * @author Baidyanath
 */
public class Vehicle {
    private final String numberPlate;
    private final VehicleType type;

    public Vehicle(String licensePlate, VehicleType type) throws InvalidArgumentException {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new InvalidArgumentException("License plate cannot be null or empty");
        }
        if (type == null) {
            throw new InvalidArgumentException("Vehicle type cannot be null");
        }
        this.numberPlate = licensePlate;
        this.type = type;
    }

    public String getLicensePlate() {
        return numberPlate;
    }

    public VehicleType getType() {
        return type;
    }
}

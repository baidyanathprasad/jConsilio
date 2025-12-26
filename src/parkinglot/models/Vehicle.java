package parkinglot.models;

import parkinglot.enums.VehicleType;

/**
 * Represents a vehicle.
 * @author Baidyanath
 */
public class Vehicle {
    private final String numberPlate;
    private final VehicleType type;

    public Vehicle(String licensePlate, VehicleType type) {
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

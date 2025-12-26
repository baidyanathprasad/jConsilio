package parkinglot.models;

import parkinglot.enums.ParkingSpotType;

import java.util.Objects;

/**
 * Represents a parking spot in the parking lot.
 * @author Baidyanath
 */
public class ParkingSpot {
    private final String spotId;
    private boolean isOccupied;
    private final ParkingSpotType parkingSpotType;
    private Vehicle vehicle;

    public ParkingSpot(String spotId, ParkingSpotType parkingSpotType) {
        this.spotId = spotId;
        this.isOccupied = false;
        this.parkingSpotType = parkingSpotType;
        this.vehicle = null;
    }

    public String getSpotId() {
        return spotId;
    }

    public boolean getIsOccupied() {
        return this.isOccupied;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public ParkingSpotType getParkingSpotType() {
        return this.parkingSpotType;
    }

    public String parkVehicle(Vehicle vehicle) {
        if (isAssignable(vehicle)) {
            this.vehicle = vehicle;
            this.isOccupied = true;
            return this.spotId;
        }
        return null; // No spot available
    }

    public void removeVehicle() {
        this.vehicle = null;
        this.isOccupied = false;
    }

    // Public method to check if a vehicle can be assigned to this spot
    public boolean isAssignable(Vehicle vehicle) {
        if (vehicle == null) return false;
        return !isOccupied && (Objects.equals(this.parkingSpotType.getDisplayName(), vehicle.getType().name()));
    }
}

package parkinglot.enums;

import parkinglot.models.ParkingSpot;
import parkinglot.models.Vehicle;
import parkinglot.exceptions.InvalidArgumentException;
import parkinglot.exceptions.InvalidExitException;
import java.time.LocalDateTime;
import static java.time.Duration.between;

/**
 * Represents a parking ticket.
 * @author Baidyanath
 */
public class Ticket {
    private final String ticketNumber;
    private final ParkingSpot parkingSpot;
    private final LocalDateTime entryTime;

    public Ticket(String ticketNumber, ParkingSpot parkingSpot, LocalDateTime entryTime) throws InvalidArgumentException {
        if (ticketNumber == null || ticketNumber.trim().isEmpty()) {
            throw new InvalidArgumentException("Ticket number cannot be null or empty");
        }
        if (parkingSpot == null) {
            throw new InvalidArgumentException("Parking spot cannot be null");
        }
        if (entryTime == null) {
            throw new InvalidArgumentException("Entry time cannot be null");
        }
        this.ticketNumber = ticketNumber;
        this.parkingSpot = parkingSpot;
        this.entryTime = entryTime;
    }

    public String getTicketNumber() {
        return this.ticketNumber;
    }

    public ParkingSpot getParkingSpot() {
        return this.parkingSpot;
    }

    public LocalDateTime getEntryTime() {
        return this.entryTime;
    }

    /**
     * calculate parking fees -
     * BIKE - consider first 1 hour $10, next every hour $5
     * CAR - consider first 1 hour $15, next every hour $10
     * TRUCK - consider first 1 hour $20, next every hour $15
     * @param exitTime exit time of the vehicle
     * @return calculated parking fees
     */
    public Double calculateParkingFees(LocalDateTime exitTime) throws InvalidExitException {
        if (exitTime == null) {
            throw new InvalidExitException("exitTime cannot be null");
        }
        if (exitTime.isBefore(this.entryTime)) {
            throw new InvalidExitException("exitTime cannot be before entryTime");
        }

        // Check if vehicle still exists in the spot
        Vehicle vehicle = this.parkingSpot.getVehicle();
        if (vehicle == null) {
            throw new IllegalStateException("Vehicle no longer in parking spot. Spot may have been cleared.");
        }

        long durationInHours = between(this.entryTime, exitTime).toHours();
        // If vehicle stayed less than an hour but Duration.toHours() returns 0, treat it as 1 hour for pricing
        if (durationInHours <= 1) {
            return switch (vehicle.getType()) {
                case BIKE -> 10.0;
                case CAR -> 15.0;
                case TRUCK -> 20.0;
            };
        } else {
            long extraHours = durationInHours - 1;
            return switch (vehicle.getType()) {
                case BIKE -> 10.0 + extraHours * 5.0;
                case CAR -> 15.0 + extraHours * 10.0;
                case TRUCK -> 20.0 + extraHours * 15.0;
            };
        }
    }
}

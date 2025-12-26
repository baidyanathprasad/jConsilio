package parkinglot.service;

import parkinglot.enums.Ticket;
import parkinglot.exceptions.InvalidExitException;
import parkinglot.models.Vehicle;

import java.time.LocalDateTime;

import static java.time.Duration.between;

/**
 * Service class for managing parking tickets.
 * @author Baidyanath
 */
public class TicketService {
    /**
     * calculate parking fees -
     * BIKE - consider first 1 hour $10, next every hour $5
     * CAR - consider first 1 hour $15, next every hour $10
     * TRUCK - consider first 1 hour $20, next every hour $15
     * @param exitTime exit time of the vehicle
     * @return calculated parking fees
     */
    public Double calculateParkingFees(Ticket ticket, LocalDateTime exitTime) throws InvalidExitException {
        if (exitTime == null) {
            throw new InvalidExitException("exitTime cannot be null");
        }
        if (exitTime.isBefore(ticket.getEntryTime())) {
            throw new InvalidExitException("exitTime cannot be before entryTime");
        }

        // Check if vehicle still exists in the spot
        Vehicle vehicle = ticket.getParkingSpot().getVehicle();
        if (vehicle == null) {
            throw new IllegalStateException("Vehicle no longer in parking spot. Spot may have been cleared.");
        }

        long durationInHours = between(ticket.getEntryTime(), exitTime).toHours();
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

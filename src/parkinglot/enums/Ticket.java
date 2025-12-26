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
}

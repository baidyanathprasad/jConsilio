package parkinglot.service;

import parkinglot.enums.Ticket;
import parkinglot.exceptions.InvalidExitException;
import parkinglot.models.Level;
import parkinglot.models.ParkingSpot;
import parkinglot.models.Vehicle;
import parkinglot.exceptions.InvalidArgumentException;
import parkinglot.exceptions.ParkingFailedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents a parking lot.
 * @author Baidyanath
 */
public class ParkingLot {
    private static volatile ParkingLot instance;
    private final List<Level> levels = new CopyOnWriteArrayList<>();
    private final Map<String, Ticket> activeTickets = new ConcurrentHashMap<>();
    private TicketService ticketService = new TicketService();

    private ParkingLot() { }

    // Make it singleton class
    public static ParkingLot getInstance() {
        if (instance == null) {
            synchronized (ParkingLot.class) {
                if (instance == null) {
                    instance = new ParkingLot();
                }
            }
        }
        return instance;
    }

    // Add levels to parking lot
    public void addLevel(Level level) {
        levels.add(level);
    }

    // add ticket to active tickets
    public void addActiveTicket(String ticketNumber, Ticket ticket) {
        activeTickets.put(ticketNumber, ticket);
    }

    public List<Level> getLevels() {
        return levels;
    }

    public Map<String, Ticket> getActiveTickets() {
        return activeTickets;
    }

    /**
     * Try to find an available spot for vehicle across all levels and return the ParkingSpot, or null.
     * This method is synchronized to ensure thread-safe spot search.
     */
    private synchronized ParkingSpot getAvailableSpotOnAnyLevel(Level level, Vehicle vehicle) {
        if (vehicle == null) return null;
        for (ParkingSpot spot : level.getParkingSpots()) {
            if (spot.isAssignable(vehicle)) {
                return spot;
            }
        }
        return null;
    }

    /**
     * Try to park a vehicle in the parking lot. If successful, create and register a Ticket, and return it.
     * If parking fails, return null.
     *
     * This method is synchronized to prevent race conditions where multiple threads could find
     * the same spot available and attempt to park in it concurrently. Synchronization ensures
     * atomic find-and-park operation.
     *
     * @param level the level to park in
     * @param vehicle the vehicle to park
     * @return the parking ticket if successful, null if no spot available
     * @throws InvalidArgumentException if level or vehicle is null
     * @throws ParkingFailedException if parking operation fails
     */
    public synchronized Ticket parkVehicle(Level level, Vehicle vehicle) throws ParkingFailedException, InvalidArgumentException {
        if (level == null) {
            throw new InvalidArgumentException("Level cannot be null");
        }
        if (vehicle == null) {
            throw new InvalidArgumentException("Vehicle cannot be null");
        }

        ParkingSpot spot = getAvailableSpotOnAnyLevel(level, vehicle);
        if (spot == null) {
            throw new ParkingFailedException("No available spot for " + vehicle.getType().name() +
                    " vehicle with license plate: " + vehicle.getLicensePlate());
        }

        String spotId = spot.parkVehicle(vehicle);
        if (spotId == null) {
            throw new ParkingFailedException("Failed to park vehicle at spot: " + spot.getSpotId());
        }

        Ticket ticket = new Ticket("TICKET-" + System.currentTimeMillis(), spot, LocalDateTime.now());
        addActiveTicket(ticket.getTicketNumber(), ticket);
        return ticket;
    }

    /**
     * Exit a vehicle from the parking lot.
     * Calculates the parking fees, removes the vehicle from the spot, and unregisters the ticket.
     *
     * @param ticket the parking ticket for the vehicle
     * @param exitTime the time when the vehicle is exiting
     * @return the calculated parking fees
     * @throws InvalidArgumentException if ticket or exitTime is null
     * @throws IllegalStateException if the ticket is not found in active tickets or vehicle not in spot
     */
    public synchronized Double exitVehicle(Ticket ticket, LocalDateTime exitTime)
            throws InvalidArgumentException, InvalidExitException {
        if (ticket == null) {
            throw new InvalidArgumentException("Ticket cannot be null");
        }
        if (exitTime == null) {
            throw new InvalidArgumentException("Exit time cannot be null");
        }

        // Check if ticket exists in active tickets
        if (!activeTickets.containsKey(ticket.getTicketNumber())) {
            throw new IllegalStateException("Ticket not found in active tickets: " + ticket.getTicketNumber());
        }

        // Calculate parking fees
        Double fees = ticketService.calculateParkingFees(ticket, exitTime);

        // Remove vehicle from spot
        ParkingSpot spot = ticket.getParkingSpot();
        spot.removeVehicle();

        // Unregister the ticket
        activeTickets.remove(ticket.getTicketNumber());

        return fees;
    }

    public TicketService getTicketService() {
        return ticketService;
    }

    public void setTicketService(TicketService ticketService) {
        this.ticketService = ticketService;
    }
}

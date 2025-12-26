package parkinglot;

import parkinglot.enums.Ticket;
import parkinglot.models.Level;
import parkinglot.models.ParkingSpot;
import parkinglot.models.Vehicle;

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
     */
    public synchronized Ticket parkVehicle(Level level, Vehicle vehicle) {
        ParkingSpot spot = getAvailableSpotOnAnyLevel(level, vehicle);
        if (spot == null) return null;
        String spotId = spot.parkVehicle(vehicle);
        if (spotId == null) return null; // Parking failed

        Ticket ticket = new Ticket("TICKET-" + System.currentTimeMillis(), spot, LocalDateTime.now());
        addActiveTicket(ticket.getTicketNumber(), ticket);
        return ticket;
    }
}

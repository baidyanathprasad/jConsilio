package parkinglot;

import parkinglot.enums.ParkingSpotType;
import parkinglot.enums.Ticket;
import parkinglot.enums.VehicleType;
import parkinglot.models.Level;
import parkinglot.models.ParkingSpot;
import parkinglot.models.Vehicle;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a parking lot.
 * @author Baidyanath
 */
public class ParkingLot {
    private static volatile ParkingLot instance;
    private final List<Level> levels = new ArrayList<>();
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
     */
    public ParkingSpot getAvailableSpotOnAnyLevel(Level level, Vehicle vehicle) {
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
     */
    public Ticket parkVehicle(Level level, Vehicle vehicle) {
        ParkingSpot spot = getAvailableSpotOnAnyLevel(level, vehicle);
        if (spot == null) return null;
        String spotId = spot.parkVehicle(vehicle);
        if (spotId == null) return null; // Parking failed

        Ticket ticket = new Ticket("TICKET-" + System.currentTimeMillis(), spot, LocalDateTime.now());
        addActiveTicket(ticket.getTicketNumber(), ticket);
        return ticket;
    }

    // Now Illustrate how to use ParkingLot class
    public static void main(String[] args) {
        ParkingLot parkingLot = ParkingLot.getInstance();
        System.out.println("Parking Lot instance created: " + parkingLot);

        // add levels to parking lot
        Level level1 = new Level(1, new ArrayList<>() {{
            add(new ParkingSpot("L1-S1", ParkingSpotType.SMALL));
            add(new ParkingSpot("L1-S3", parkinglot.enums.ParkingSpotType.MEDIUM));
            add(new ParkingSpot("L1-S4", ParkingSpotType.LARGE));
        }}); // Make it 5 spots for brevity
        parkingLot.addLevel(level1);
        System.out.println("Added Level 1 to Parking Lot. Total levels: " + parkingLot.getLevels().size());

        // Park a vehicle and create a ticket (omitting actual parking logic for brevity)
        Vehicle bike1 = new Vehicle("ABC123", VehicleType.BIKE);
        Ticket ticket = parkingLot.parkVehicle(level1, bike1);
        if (ticket != null) {
            System.out.println("Parked bike with license plate " + bike1.getLicensePlate() + " at spot " + ticket.getSpotId().getSpotId());
        } else {
            System.out.println("No available spot for bike with license plate " + bike1.getLicensePlate());
        }

        // Park vehicle 2
        Vehicle bike2 = new Vehicle("XYZ789", VehicleType.BIKE);
        Ticket ticket2 = parkingLot.parkVehicle(level1, bike2);
        if (ticket2 != null) {
            System.out.println("Parked bike with license plate " + bike2.getLicensePlate() + " at spot " + ticket2.getSpotId().getSpotId());
        } else {
            System.out.println("No available spot for bike with license plate " + bike2.getLicensePlate());
        }

        // Park vehicle 3
        Vehicle car1 = new Vehicle("XYZ789", VehicleType.CAR);
        Ticket ticket3 = parkingLot.parkVehicle(level1, car1);
        if (ticket3 != null) {
            System.out.println("Parked car with license plate " + car1.getLicensePlate() + " at spot " + ticket3.getSpotId().getSpotId());
        } else {
            System.out.println("No available spot for car with license plate " + car1.getLicensePlate());
        }

        // Park vehicle 4
        Vehicle truck1 = new Vehicle("LMN456", VehicleType.TRUCK);
        Ticket ticket4 = parkingLot.parkVehicle(level1, truck1);
        if (ticket4 != null) {
            System.out.println("Parked truck with license plate " + truck1.getLicensePlate() + " at spot " + ticket4.getSpotId().getSpotId());
        } else {
            System.out.println("No available spot for truck with license plate " + truck1.getLicensePlate());
        }

        // Remove the vehicle1
        if (ticket != null) {
            // calculate parking fees
            Double fees = ticket.calculateParkingFees(LocalDateTime.now());
            System.out.println("Parking fees for vehicle with license plate " + bike1.getLicensePlate() + ": $" + fees);

            // Clear the spot and remove ticket
            ParkingSpot spot = ticket.getSpotId();
            spot.removeVehicle();
            parkingLot.getActiveTickets().remove(ticket.getTicketNumber());
            System.out.println("Removed vehicle with license plate " + bike1.getLicensePlate() + " from spot " + spot.getSpotId());
        }

        // Now park vehicle2 again
        Ticket ticket5 = parkingLot.parkVehicle(level1, bike2);
        if (ticket5 != null) {
            System.out.println("Parked bike with license plate " + bike2.getLicensePlate() + " at spot " + ticket5.getSpotId().getSpotId());
        } else {
            System.out.println("No available spot for bike with license plate " + bike2.getLicensePlate());
        }

        System.out.println("Active tickets in the parking lot: " + parkingLot.getActiveTickets());
    }
}

package parkinglot;

import parkinglot.enums.ParkingSpotType;
import parkinglot.enums.Ticket;
import parkinglot.enums.VehicleType;
import parkinglot.models.Level;
import parkinglot.models.ParkingSpot;
import parkinglot.models.Vehicle;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Demo/Illustration class to demonstrate ParkingLot functionality.
 * This class showcases how to use the ParkingLot system for parking and retrieving vehicles.
 * @author Baidyanath
 */
public class ParkingLotDemo {

    private final ParkingLot parkingLot;
    private final Level level1;

    public ParkingLotDemo() {
        this.parkingLot = ParkingLot.getInstance();
        this.level1 = initializeLevel();
    }

    /**
     * Initialize parking level with spots for different vehicle types.
     */
    private Level initializeLevel() {
        Level level = new Level(1, new ArrayList<>() {{
            add(new ParkingSpot("L1-S1", ParkingSpotType.SMALL));
            add(new ParkingSpot("L1-S3", ParkingSpotType.MEDIUM));
            add(new ParkingSpot("L1-S4", ParkingSpotType.LARGE));
        }});
        parkingLot.addLevel(level);
        return level;
    }

    /**
     * Set up the parking lot infrastructure.
     */
    public void setupParkingLot() {
        System.out.println("========== PARKING LOT SETUP ==========");
        System.out.println("Parking Lot instance created: " + parkingLot);
        System.out.println("Added Level 1 to Parking Lot. Total levels: " + parkingLot.getLevels().size());
        System.out.println("Level 1 has " + level1.getParkingSpots().size() + " parking spots\n");
    }

    /**
     * Park a vehicle and display the result.
     */
    private void parkVehicleAndDisplay(Vehicle vehicle) {
        try {
            System.out.println("Attempting to park " + vehicle.getType().name().toLowerCase() +
                    " with license plate: " + vehicle.getLicensePlate());

            Ticket ticket = parkingLot.parkVehicle(level1, vehicle);
            if (ticket != null) {
                System.out.println("✓ Successfully parked at spot: " + ticket.getParkingSpot().getSpotId());
                System.out.println("Ticket ID: " + ticket.getTicketNumber());
            } else {
                System.out.println("✗ No available spot for " + vehicle.getType().name().toLowerCase() +
                        " with license plate: " + vehicle.getLicensePlate());
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error while parking vehicle: " + e.getMessage());
        }
    }

    /**
     * Demo: Park multiple vehicles.
     */
    public void demoParking() {
        System.out.println("========== PARKING VEHICLES ==========");

        Vehicle bike1 = new Vehicle("ABC123", VehicleType.BIKE);
        parkVehicleAndDisplay(bike1);

        Vehicle bike2 = new Vehicle("XYZ789", VehicleType.BIKE);
        parkVehicleAndDisplay(bike2);

        Vehicle car1 = new Vehicle("CAR001", VehicleType.CAR);
        parkVehicleAndDisplay(car1);

        Vehicle truck1 = new Vehicle("TRUCK01", VehicleType.TRUCK);
        parkVehicleAndDisplay(truck1);
    }

    /**
     * Demo: Remove a vehicle, calculate fees, and re-park another vehicle in the freed spot.
     */
    public void demoRemoveAndRepark() {
        System.out.println("========== REMOVE VEHICLE & RE-PARK ==========");

        // Get the first ticket (bike1)
        Ticket firstTicket = parkingLot.getActiveTickets()
                .values()
                .stream()
                .filter(it -> it.getParkingSpot().getVehicle().getType() == VehicleType.BIKE)
                .findFirst()
                .orElse(null);
        System.out.println("Removing vehicle with Ticket ID: " +
                (firstTicket != null ? firstTicket.getTicketNumber() : "N/A"));

        if (firstTicket != null) {
            try {
                Vehicle vehicle = firstTicket.getParkingSpot().getVehicle();

                // Calculate parking fees
                Double fees = firstTicket.calculateParkingFees(LocalDateTime.now());
                System.out.println("Vehicle: " + vehicle.getLicensePlate());
                System.out.println("Parking fees: $" + String.format("%.2f", fees));

                // Remove vehicle from spot
                ParkingSpot spot = firstTicket.getParkingSpot();
                spot.removeVehicle();
                parkingLot.getActiveTickets().remove(firstTicket.getTicketNumber());
                System.out.println("✓ Removed vehicle from spot: " + spot.getSpotId());
                System.out.println("✓ Spot is now available: " + !spot.getIsOccupied());
                System.out.println();

                // Re-park another vehicle in the freed spot
                Vehicle bike3 = new Vehicle("NEW999", VehicleType.BIKE);
                System.out.println("Re-parking new vehicle in freed spot...");
                parkVehicleAndDisplay(bike3);
            } catch (Exception e) {
                System.out.println("Error during remove and re-park: " + e.getMessage());
            }
        } else {
            System.out.println("No tickets found to remove.");
        }
    }

    /**
     * Display active tickets and parking status.
     */
    public void displayParkingStatus() {
        System.out.println("========== PARKING STATUS ==========");
        System.out.println("Total active tickets: " + parkingLot.getActiveTickets().size());
        System.out.println("\nSpot Status:");
        for (ParkingSpot spot : level1.getParkingSpots()) {
            String status = spot.getIsOccupied() ? "OCCUPIED" : "AVAILABLE";
            String vehicleInfo = spot.getVehicle() != null ?
                    " - " + spot.getVehicle().getType().name() + " (" + spot.getVehicle().getLicensePlate() + ")" : "";
            System.out.println("  " + spot.getSpotId() + " [" + status + "]" + vehicleInfo);
        }
        System.out.println();
    }

    /**
     * Run the complete demo.
     */
    public void runDemo() {
        setupParkingLot();
        demoParking();
        displayParkingStatus();
        demoRemoveAndRepark();
        displayParkingStatus();
    }

    /**
     * Main entry point for the demo.
     */
    public static void main(String[] args) {
        ParkingLotDemo demo = new ParkingLotDemo();
        demo.runDemo();
    }
}


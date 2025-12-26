package parkinglot;

import parkinglot.enums.ParkingSpotType;
import parkinglot.enums.Ticket;
import parkinglot.enums.VehicleType;
import parkinglot.exceptions.InvalidArgumentException;
import parkinglot.models.Level;
import parkinglot.models.ParkingSpot;
import parkinglot.models.Vehicle;
import parkinglot.service.ParkingLot;

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

    public ParkingLotDemo() throws InvalidArgumentException {
        this.parkingLot = ParkingLot.getInstance();
        this.level1 = initializeLevel();
    }

    /**
     * Initialize parking level with spots for different vehicle types.
     */
    private Level initializeLevel() throws InvalidArgumentException {
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
            System.out.println("✓ Successfully parked at spot: " + ticket.getParkingSpot().getSpotId());
            System.out.println("  Ticket ID: " + ticket.getTicketNumber());
        } catch (Exception e) {
            System.out.println("✗ Unexpected error while parking vehicle: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Demo: Park multiple vehicles.
     */
    public void demoParking() throws InvalidArgumentException {
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
     * Demo: Exit a vehicle, calculate fees, and re-park another vehicle in the freed spot.
     */
    public void demoExitAndRepark() {
        System.out.println("========== EXIT VEHICLE & RE-PARK ==========");

        // Get the first bike ticket
        Ticket firstTicket = parkingLot.getActiveTickets()
                .values()
                .stream()
                .filter(it -> it.getParkingSpot().getVehicle() != null &&
                        it.getParkingSpot().getVehicle().getType() == VehicleType.BIKE
                )
                .findFirst()
                .orElse(null);

        if (firstTicket != null) {
            try {
                Vehicle vehicle = firstTicket.getParkingSpot().getVehicle();
                String spotId = firstTicket.getParkingSpot().getSpotId();

                System.out.println("Vehicle exiting:");
                System.out.println("  License Plate: " + vehicle.getLicensePlate());
                System.out.println("  Spot: " + spotId);
                System.out.println("  Entry Time: " + firstTicket.getEntryTime());

                // Exit vehicle and calculate fees
                Double fees = parkingLot.exitVehicle(firstTicket, LocalDateTime.now());
                System.out.println("  Exit Time: " + LocalDateTime.now());
                System.out.println("✓ Parking fees: $" + String.format("%.2f", fees));
                System.out.println("✓ Vehicle exited successfully from spot: " + spotId);
                System.out.println();

                // Re-park another vehicle in the freed spot
                Vehicle bike3 = new Vehicle("NEW999", VehicleType.BIKE);
                System.out.println("Re-parking new vehicle in freed spot...");
                parkVehicleAndDisplay(bike3);
            } catch (Exception e) {
                System.out.println("✗ Error during exit: " + e.getMessage());
            }
        } else {
            System.out.println("No bike tickets found to exit.");
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
    public void runDemo() throws InvalidArgumentException {
        setupParkingLot();
        demoParking();
        displayParkingStatus();
        demoExitAndRepark();
        displayParkingStatus();
    }

    /**
     * Main entry point for the demo.
     */
    public static void main(String[] args) {
        try {
            ParkingLotDemo demo = new ParkingLotDemo();
            demo.runDemo();
        } catch (InvalidArgumentException e) {
            System.out.println("✗ Error in demo execution: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Unexpected error in demo execution: " + e.getMessage());
        }
    }
}


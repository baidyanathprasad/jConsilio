package parkinglot.exceptions;

/**
 * Exception thrown when a vehicle cannot be parked (e.g., no available spots).
 * @author Baidyanath
 */
public class ParkingFailedException extends ParkingLotException {
    public ParkingFailedException(String message) {
        super(message);
    }

    public ParkingFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}



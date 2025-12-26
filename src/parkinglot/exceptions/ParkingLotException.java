package parkinglot.exceptions;

/**
 * Base exception for all parking lot related errors.
 * @author Baidyanath
 */
public class ParkingLotException extends Exception {
    public ParkingLotException(String message) {
        super(message);
    }

    public ParkingLotException(String message, Throwable cause) {
        super(message, cause);
    }
}


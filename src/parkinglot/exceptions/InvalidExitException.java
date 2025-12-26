package parkinglot.exceptions;

/**
 * Exception thrown when trying to exit a vehicle with invalid ticket or parameters.
 * @author Baidyanath
 */
public class InvalidExitException extends ParkingLotException {
    public InvalidExitException(String message) {
        super(message);
    }

    public InvalidExitException(String message, Throwable cause) {
        super(message, cause);
    }
}


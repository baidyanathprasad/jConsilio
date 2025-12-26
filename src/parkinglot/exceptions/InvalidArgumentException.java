package parkinglot.exceptions;

/**
 * Exception thrown when an invalid argument is provided to parking lot operations.
 * @author Baidyanath
 */
public class InvalidArgumentException extends ParkingLotException {
    public InvalidArgumentException(String message) {
        super(message);
    }

    public InvalidArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}


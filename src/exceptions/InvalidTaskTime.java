package exceptions;

public class InvalidTaskTime extends Exception {
    public InvalidTaskTime(String invalidTaskTime) {
        super(invalidTaskTime);
    }
}

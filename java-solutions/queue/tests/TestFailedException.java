package queue.tests;

public class TestFailedException extends RuntimeException {
    public TestFailedException(String message) {
        super("Test failed: " + message);
    }
}

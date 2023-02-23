package queue.tests;

public class ContractFailure extends AssertionError {
    public ContractFailure(String message) {
        super("Contract failure:  " + message);
    }
    public ContractFailure(String methodName, String description) {
        super("Contract failure of method " + methodName +
                "\nDescription: " + description);
    }
}

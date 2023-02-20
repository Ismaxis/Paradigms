package queue.tests;

public class ArrayQueueMyTest {
    public static void main(String[] args) {
        ArrayQueueTester[] testers = {
                new ArrayQueueModuleTester(),
                new ArrayQueueADTTester(),
                new ArrayQueueClassTester()
        };

        try {
            for (ArrayQueueTester tester : testers) {
                tester.test();
            }
        } catch (TestFailedException e) {
            System.err.println(e.getMessage());
        }
    }
}

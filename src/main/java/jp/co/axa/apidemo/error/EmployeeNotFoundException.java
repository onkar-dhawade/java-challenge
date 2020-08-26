package jp.co.axa.apidemo.error;

/**
 * Custom exception class when mentioned employee is not present
 */
public class EmployeeNotFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    EmployeeNotFoundException() {
        super();
    }

    public EmployeeNotFoundException(Long employeeId) {
        super("Could not find employee with id: " + employeeId);
    }
}
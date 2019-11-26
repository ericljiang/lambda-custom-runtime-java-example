package lambda.custom.runtime.java.example.runtime;

public class LambdaInvocationError extends Exception {

    private static final long serialVersionUID = 1L;

    public LambdaInvocationError(String message, Throwable cause) {
        super(message, cause);
    }

    public LambdaInvocationError(String message) {
        super(message);
    }
}

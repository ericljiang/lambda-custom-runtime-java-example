package lambda.custom.runtime.java.example.runtime;

public class LambdaRuntimeError extends Exception {

    private static final long serialVersionUID = 1L;

    public LambdaRuntimeError(String message, Throwable cause) {
        super(message, cause);
    }

    public LambdaRuntimeError(String message) {
        super(message);
    }

    public LambdaRuntimeError(Throwable cause) {
        super(cause);
    }
}

package lambda.custom.runtime.java.example.runtime;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import lambda.custom.runtime.java.example.runtime.exception.LambdaRuntimeError;

public class LambdaRuntimeTest {
    @Test
    public void succeeds() throws LambdaRuntimeError {
        final LambdaRuntime runtime = LambdaRuntime.create("localhost:1234");
        assertNotNull(runtime);
    }
}

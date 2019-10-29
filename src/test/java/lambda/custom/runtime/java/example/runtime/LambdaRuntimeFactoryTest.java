package lambda.custom.runtime.java.example.runtime;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class LambdaRuntimeFactoryTest {
    @Test
    public void succeeds() throws LambdaRuntimeError {
        final LambdaRuntime runtime = LambdaRuntimeFactory.createLambdaRuntime("localhost:1234");
        assertNotNull(runtime);
    }
}

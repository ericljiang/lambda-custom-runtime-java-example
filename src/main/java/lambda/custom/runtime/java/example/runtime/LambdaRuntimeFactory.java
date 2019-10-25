package lambda.custom.runtime.java.example.runtime;

import java.util.Optional;

public class LambdaRuntimeFactory {

    private LambdaRuntimeFactory() {}

    public static LambdaRuntime createLambdaRuntime() {
        final String runtimeApiEndpoint = Optional.ofNullable(System.getenv("AWS_LAMBDA_RUNTIME_API"))
                .orElseThrow(() -> new RuntimeException("Environment variable 'AWS_LAMBDA_RUNTIME_API' is not set."));
        final LambdaRuntimeInterface lambdaRuntimeInterface = LambdaRuntimeInterfaceClient.builder()
                .runtimeApiEndpoint(runtimeApiEndpoint)
                .build();
        final LambdaInvocationPoller lambdaInvocationPoller = LambdaInvocationPoller.builder()
                .lambdaRuntimeInterface(lambdaRuntimeInterface)
                .build();
        final LambdaRuntime runtime = LambdaRuntime.builder()
                .lambdaRuntimeInterface(lambdaRuntimeInterface)
                .lambdaInvocationPoller(lambdaInvocationPoller)
                .build();
        return runtime;
    }
}

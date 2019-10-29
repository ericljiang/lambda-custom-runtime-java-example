package lambda.custom.runtime.java.example.runtime;

public class LambdaRuntimeFactory {

    private LambdaRuntimeFactory() {}

    public static LambdaRuntime createLambdaRuntime(String runtimeApiDomain) {
        final String runtimeApiEndpoint = String.format("http://%s", runtimeApiDomain);
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

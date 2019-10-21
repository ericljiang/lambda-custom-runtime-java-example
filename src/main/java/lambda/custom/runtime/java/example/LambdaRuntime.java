package lambda.custom.runtime.java.example;

import java.util.Optional;

import com.amazonaws.services.lambda.runtime.RequestHandler;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class LambdaRuntime {

    private final LambdaRuntimeInterface lambdaRuntimeInterface;

    private final RequestHandler<String, String> requestHandler;

    public void initialize() throws Exception {
        while (true) {
            final String request = lambdaRuntimeInterface.getNextInvocation();
            final String awsRequestId = "";
            try {
                final String response = this.requestHandler.handleRequest(request, LambdaContext.builder().build());
                lambdaRuntimeInterface.postInvocationResponse(awsRequestId, response);
            } catch (Exception e) {
                lambdaRuntimeInterface.postInvocationError(awsRequestId, e);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        log.info("Creating client for Lambda Runtime Interface...");
        final String runtimeApiEndpoint = Optional.ofNullable(System.getenv("AWS_LAMBDA_RUNTIME_API"))
                .orElseThrow(() -> new RuntimeException("Environment variable 'AWS_LAMBDA_RUNTIME_API' is not set."));

        final LambdaRuntimeInterface lambdaRuntimeInterface = LambdaRuntimeInterfaceClient.builder()
                .runtimeApiEndpoint(runtimeApiEndpoint)
                .build();

        try {
            log.info("Initializing runtime...");
            final LambdaRuntime runtime = LambdaRuntime.builder()
                    .lambdaRuntimeInterface(lambdaRuntimeInterface)
                    .requestHandler(new Handler())
                    .build();
            runtime.initialize();
        } catch (Exception e) {
            log.error("Error occured during runtime initialization. Attempting to post to runtime interface.", e);
            lambdaRuntimeInterface.postInitializationError(e);
        }
    }
}

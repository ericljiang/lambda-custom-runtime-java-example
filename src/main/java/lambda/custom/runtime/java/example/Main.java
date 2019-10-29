package lambda.custom.runtime.java.example;

import java.util.Optional;

import lambda.custom.runtime.java.example.runtime.LambdaRuntime;
import lambda.custom.runtime.java.example.runtime.LambdaRuntimeError;
import lambda.custom.runtime.java.example.runtime.LambdaRuntimeFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("Initializing runtime...");
        final String runtimeApiDomain = Optional.ofNullable(System.getenv("AWS_LAMBDA_RUNTIME_API"))
                .orElseThrow(() -> new RuntimeException("Environment variable 'AWS_LAMBDA_RUNTIME_API' is not set."));
        final LambdaRuntime runtime = LambdaRuntimeFactory.createLambdaRuntime(runtimeApiDomain);
        try {
            runtime.initialize(new Handler());
        } catch (LambdaRuntimeError e) {
            log.error("Unhandled exception", e);
        }
    }
}

package lambda.custom.runtime.java.example;

import lambda.custom.runtime.java.example.runtime.LambdaRuntime;
import lambda.custom.runtime.java.example.runtime.LambdaRuntimeFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("Initializing runtime...");
        final LambdaRuntime runtime = LambdaRuntimeFactory.createLambdaRuntime();
        runtime.initialize(new Handler(), Request.class, Response.class);
    }
}

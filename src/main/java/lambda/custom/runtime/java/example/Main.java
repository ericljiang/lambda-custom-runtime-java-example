package lambda.custom.runtime.java.example;

import lambda.custom.runtime.java.example.runtime.LambdaRuntime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("Initializing runtime...");
        final LambdaRuntime runtime = LambdaRuntime.create();
        runtime.initialize(new Handler(), Request.class, Response.class);
    }
}

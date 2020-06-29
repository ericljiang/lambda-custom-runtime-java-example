package lambda.custom.runtime.java.example;

import lombok.extern.slf4j.Slf4j;
import me.ericjiang.aws.lambda.graalvm.LambdaRuntime;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("Initializing runtime...");
        final LambdaRuntime runtime = LambdaRuntime.create();
        runtime.initialize(new Handler(), Request.class, Response.class);
    }
}

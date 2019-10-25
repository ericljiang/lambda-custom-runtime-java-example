package lambda.custom.runtime.java.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Handler implements RequestHandler<String, String> {

    @Override
    public String handleRequest(String input, Context context) {
        log.info("Received request.");
        // return Response.builder()
        //         .message("Hello world")
        //         .build();
        return "Hello world";
    }

}

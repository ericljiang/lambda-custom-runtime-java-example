package lambda.custom.runtime.java.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Handler implements RequestHandler<Request, Response> {
    @Override
    public Response handleRequest(Request input, Context context) {
        return Response.builder()
                .message("Hello world")
                .build();
    }
}

package lambda.custom.runtime.java.example.runtime;

import com.amazonaws.services.lambda.runtime.RequestHandler;

import lambda.custom.runtime.java.example.runtime.serialization.RequestDeserializer;
import lambda.custom.runtime.java.example.runtime.serialization.ResponseSerializer;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class LambdaRuntime {

    @NonNull
    private final LambdaRuntimeInterface lambdaRuntimeInterface;

    @NonNull
    private final LambdaInvocationPoller lambdaInvocationPoller;

    public void initialize(RequestHandler<String, String> requestHandler) throws LambdaRuntimeError {
        initialize(requestHandler, s -> s, s -> s);
    }

    public <I> void initialize(RequestHandler<I, String> requestHandler,
            RequestDeserializer<I> requestDeserializer) throws LambdaRuntimeError {
        initialize(requestHandler, requestDeserializer, s -> s);
    }

    public <O> void initialize(RequestHandler<String, O> requestHandler,
            ResponseSerializer<O> responseSerializer) throws LambdaRuntimeError {
        initialize(requestHandler, s -> s, responseSerializer);
    }

    public <I, O> void initialize(RequestHandler<I, O> requestHandler,
            RequestDeserializer<I> requestDeserializer,
            ResponseSerializer<O> responseSerializer) throws LambdaRuntimeError {
        try {
            while (true) {
                this.lambdaInvocationPoller.pollAndHandleInvocation(
                        requestHandler,
                        requestDeserializer,
                        responseSerializer);
            }
        } catch (LambdaRuntimeError e) {
            log.error("Error occured during runtime initialization. Attempting to post to runtime interface.", e);
            lambdaRuntimeInterface.postInitializationError(e);
            log.info("Successfully posted initialization error to runtime interface. Exiting.");
        }
    }
}

package lambda.custom.runtime.java.example.runtime;

import com.amazonaws.services.lambda.runtime.RequestHandler;

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
        try {
            while (true) {
                this.lambdaInvocationPoller.pollAndHandleInvocation(requestHandler);
            }
        } catch (LambdaRuntimeError e) {
            log.error("Error occured during runtime initialization. Attempting to post to runtime interface.", e);
            lambdaRuntimeInterface.postInitializationError(e);
            log.info("Successfully posted initialization error to runtime interface. Exiting.");
        }
    }
}

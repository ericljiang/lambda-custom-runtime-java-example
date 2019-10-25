package lambda.custom.runtime.java.example.runtime;

import com.amazonaws.services.lambda.runtime.RequestHandler;

import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class LambdaInvocationPoller {

    @NonNull
    private final LambdaRuntimeInterface lambdaRuntimeInterface;

    public void pollAndHandleInvocation(RequestHandler<String, String> requestHandler) throws LambdaRuntimeError {
        final LambdaInvocation invocation = this.lambdaRuntimeInterface.getNextInvocation();
        final String invocationEvent = invocation.getInvocationEvent();
        final LambdaContext context = invocation.getContext();
        final String awsRequestId = context.getAwsRequestId();
        try {
            final String response = requestHandler.handleRequest(invocationEvent, context);
            this.lambdaRuntimeInterface.postInvocationResponse(awsRequestId, response);
        } catch (RuntimeException e) {
            log.error("Error while handling request.", e);
            this.lambdaRuntimeInterface.postInvocationError(awsRequestId, e);
        }
    }
}

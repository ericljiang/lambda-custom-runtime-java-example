package lambda.custom.runtime.java.example.runtime;

import com.amazonaws.services.lambda.runtime.RequestHandler;

import lambda.custom.runtime.java.example.runtime.serialization.RequestDeserializer;
import lambda.custom.runtime.java.example.runtime.serialization.ResponseSerializer;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class LambdaInvocationPoller {

    @NonNull
    private final LambdaRuntimeInterface lambdaRuntimeInterface;

    public <I, O> void pollAndHandleInvocation(RequestHandler<I, O> requestHandler,
            RequestDeserializer<I> requestDeserializer,
            ResponseSerializer<O> responseSerializer) throws LambdaRuntimeError {

        final LambdaInvocation invocation = this.lambdaRuntimeInterface.getNextInvocation();
        final String invocationEvent = invocation.getInvocationEvent();
        final LambdaContext context = invocation.getContext();
        final String awsRequestId = context.getAwsRequestId();

        try {
            final I request = requestDeserializer.deserialize(invocationEvent);
            final O response = invoke(requestHandler, request, context);
            final String serializedResponse = responseSerializer.serialize(response);
            this.lambdaRuntimeInterface.postInvocationResponse(awsRequestId, serializedResponse);
        } catch (LambdaInvocationError e) {
            this.lambdaRuntimeInterface.postInvocationError(awsRequestId, e);
        }
    }

    /**
     * Invokes the supplied handler and wraps any thrown exception in a {@link LambdaInvocationError}.
     *
     * @param <I> The type for request objects accepted by the requestHandler
     * @param <O> The type for response objects returned by the requestHandler
     * @param requestHandler The Lambda Function to invoke
     * @param request The input for the Lambda Function
     * @param context The Lambda execution environment context object.
     * @return The Lambda Function output
     * @throws LambdaInvocationError
     * @throws LambdaRuntimeError
     */
    private <I, O> O invoke(RequestHandler<I, O> requestHandler, I request, LambdaContext context)
            throws LambdaInvocationError, LambdaRuntimeError {
        try {
            return requestHandler.handleRequest(request, context);
        } catch (RuntimeException e) {
            throw new LambdaInvocationError("Lambda Function RequestHandler threw error while handling request.", e);
        }
    }
}

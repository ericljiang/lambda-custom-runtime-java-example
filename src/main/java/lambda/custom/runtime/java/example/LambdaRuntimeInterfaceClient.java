package lambda.custom.runtime.java.example;

import lombok.Builder;

@Builder
public class LambdaRuntimeInterfaceClient implements LambdaRuntimeInterface {

    private final String runtimeApiEndpoint;

    @Override
    public String getNextInvocation() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void postInvocationResponse(String awsRequestId, String invocationResponse) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void postInvocationError(String awsRequestId, Throwable error) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void postInitializationError(Throwable error) throws Exception {
        throw new UnsupportedOperationException();
    }
}

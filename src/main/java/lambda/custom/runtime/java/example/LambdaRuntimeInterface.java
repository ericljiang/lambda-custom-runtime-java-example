package lambda.custom.runtime.java.example;

public interface LambdaRuntimeInterface {
    String getNextInvocation() throws Exception;

    void postInvocationResponse(String awsRequestId, String invocationResponse) throws Exception;

    void postInvocationError(String awsRequestId, Throwable error) throws Exception;

    void postInitializationError(Throwable error) throws Exception;
}

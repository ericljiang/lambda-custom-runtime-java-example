package lambda.custom.runtime.java.example.runtime;

import lambda.custom.runtime.java.example.runtime.exception.LambdaRuntimeError;

interface LambdaRuntimeInterface {
    LambdaInvocation getNextInvocation() throws LambdaRuntimeError;

    void postInvocationResponse(String awsRequestId, String invocationResponse) throws LambdaRuntimeError;

    void postInvocationError(String awsRequestId, Throwable error) throws LambdaRuntimeError;

    void postInitializationError(Throwable error) throws LambdaRuntimeError;
}

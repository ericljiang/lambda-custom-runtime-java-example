package lambda.custom.runtime.java.example.runtime;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import lambda.custom.runtime.java.example.runtime.serialization.RequestDeserializer;
import lambda.custom.runtime.java.example.runtime.serialization.ResponseSerializer;

public class LambdaRuntimeTest extends EasyMockSupport {
    private LambdaRuntimeInterface runtimeInterface;
    private RequestHandler<String, String> requestHandler;
    private RequestDeserializer<String> requestDeserializer;
    private ResponseSerializer<String> responseSerializer;
    private LambdaInvocationPoller lambdaInvocationPoller;
    private LambdaRuntime runtime;

    @Before
    public void before() {
        this.runtimeInterface = mock(LambdaRuntimeInterface.class);
        this.requestHandler = mock(RequestHandler.class);
        this.requestDeserializer = s -> s;
        this.responseSerializer = s -> s;
        this.lambdaInvocationPoller = mock(LambdaInvocationPoller.class);
        this.runtime = LambdaRuntime.builder()
                .lambdaRuntimeInterface(this.runtimeInterface)
                .lambdaInvocationPoller(this.lambdaInvocationPoller)
                .build();
    }

    @Test
    public void postsInitializationError() throws LambdaRuntimeError {
        final LambdaRuntimeError error = new LambdaRuntimeError("");
        this.lambdaInvocationPoller.pollAndHandleInvocation(
                eq(this.requestHandler),
                EasyMock.<RequestDeserializer<String>>anyObject(),
                EasyMock.<ResponseSerializer<String>>anyObject());
        expectLastCall().andThrow(error);
        this.runtimeInterface.postInitializationError(error);
        expectLastCall();
        replayAll();

        this.runtime.initialize(this.requestHandler);
        verifyAll();
    }

    @Test
    public void postsInitializationErrorWithCustomRequest() throws LambdaRuntimeError {
        final LambdaRuntimeError error = new LambdaRuntimeError("");
        this.lambdaInvocationPoller.pollAndHandleInvocation(
                eq(this.requestHandler),
                eq(this.requestDeserializer),
                EasyMock.<ResponseSerializer<String>>anyObject());
        expectLastCall().andThrow(error);
        this.runtimeInterface.postInitializationError(error);
        expectLastCall();
        replayAll();

        this.runtime.initialize(this.requestHandler, this.requestDeserializer);
        verifyAll();
    }

    @Test
    public void postsInitializationErrorWithCustomResponse() throws LambdaRuntimeError {
        final LambdaRuntimeError error = new LambdaRuntimeError("");
        this.lambdaInvocationPoller.pollAndHandleInvocation(
                eq(this.requestHandler),
                EasyMock.<RequestDeserializer<String>>anyObject(),
                eq(this.responseSerializer));
        expectLastCall().andThrow(error);
        this.runtimeInterface.postInitializationError(error);
        expectLastCall();
        replayAll();

        this.runtime.initialize(this.requestHandler, this.responseSerializer);
        verifyAll();
    }

    @Test
    public void postsInitializationErrorWithCustomRequestResponse() throws LambdaRuntimeError {
        final LambdaRuntimeError error = new LambdaRuntimeError("");
        this.lambdaInvocationPoller.pollAndHandleInvocation(
                eq(this.requestHandler),
                eq(this.requestDeserializer),
                eq(this.responseSerializer));
        expectLastCall().andThrow(error);
        this.runtimeInterface.postInitializationError(error);
        expectLastCall();
        replayAll();

        this.runtime.initialize(this.requestHandler, this.requestDeserializer, this.responseSerializer);
        verifyAll();
    }

    @Test
    public void callsLambdaInvocationPoller() throws LambdaRuntimeError {
        this.lambdaInvocationPoller.pollAndHandleInvocation(
                eq(this.requestHandler),
                EasyMock.<RequestDeserializer<String>>anyObject(),
                EasyMock.<ResponseSerializer<String>>anyObject());
        expectLastCall().atLeastOnce();
        replayAll();

        runWithTimeout(() -> {
            try {
                this.runtime.initialize(this.requestHandler);
            } catch (LambdaRuntimeError e) {
                throw new RuntimeException(e);
            }
        }, 1, TimeUnit.SECONDS);
        verifyAll();
    }

    @Test
    public void callsLambdaInvocationPollerWithCustomRequest() throws LambdaRuntimeError {
        this.lambdaInvocationPoller.pollAndHandleInvocation(
                eq(this.requestHandler),
                eq(this.requestDeserializer),
                EasyMock.<ResponseSerializer<String>>anyObject());
        expectLastCall().atLeastOnce();
        replayAll();

        runWithTimeout(() -> {
            try {
                this.runtime.initialize(this.requestHandler, this.requestDeserializer);
            } catch (LambdaRuntimeError e) {
                throw new RuntimeException(e);
            }
        }, 1, TimeUnit.SECONDS);
        verifyAll();
    }

    @Test
    public void callsLambdaInvocationPollerWithCustomResponse() throws LambdaRuntimeError {
        this.lambdaInvocationPoller.pollAndHandleInvocation(
                eq(this.requestHandler),
                EasyMock.<RequestDeserializer<String>>anyObject(),
                eq(this.responseSerializer));
        expectLastCall().atLeastOnce();
        replayAll();

        runWithTimeout(() -> {
            try {
                this.runtime.initialize(this.requestHandler, this.responseSerializer);
            } catch (LambdaRuntimeError e) {
                throw new RuntimeException(e);
            }
        }, 1, TimeUnit.SECONDS);
        verifyAll();
    }

    @Test
    public void callsLambdaInvocationPollerWithCustomRequestResponse() throws LambdaRuntimeError {
        this.lambdaInvocationPoller.pollAndHandleInvocation(
                eq(this.requestHandler),
                eq(this.requestDeserializer),
                eq(this.responseSerializer));
        expectLastCall().atLeastOnce();
        replayAll();

        runWithTimeout(() -> {
            try {
                this.runtime.initialize(this.requestHandler, this.requestDeserializer, this.responseSerializer);
            } catch (LambdaRuntimeError e) {
                throw new RuntimeException(e);
            }
        }, 1, TimeUnit.SECONDS);
        verifyAll();
    }

    private void runWithTimeout(Runnable runnable, long timeout, TimeUnit unit) {
        final ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(runnable);
        executorService.shutdown();
        try {
            executorService.awaitTermination(timeout, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdownNow();
        }
    }

    interface ThrowingRunnable {
        void run() throws Exception;
    }
}

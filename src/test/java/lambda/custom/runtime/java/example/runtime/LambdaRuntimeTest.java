package lambda.custom.runtime.java.example.runtime;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import lambda.custom.runtime.java.example.runtime.serialization.DefaultSerializer;
import lambda.custom.runtime.java.example.runtime.serialization.RequestDeserializer;
import lambda.custom.runtime.java.example.runtime.serialization.ResponseSerializer;

public class LambdaRuntimeTest extends EasyMockSupport {
    private LambdaRuntimeInterface runtimeInterface;
    private RequestHandler<String, String> requestHandler;
    private RequestDeserializer<String> requestDeserializer;
    private ResponseSerializer<String> responseSerializer;
    private LambdaInvocationPoller lambdaInvocationPoller;
    private DefaultSerializer defaultSerializer;
    private LambdaRuntime runtime;

    @Before
    public void before() {
        this.runtimeInterface = mock(LambdaRuntimeInterface.class);
        this.requestHandler = mock(RequestHandler.class);
        this.requestDeserializer = s -> s;
        this.responseSerializer = s -> s;
        this.lambdaInvocationPoller = mock(LambdaInvocationPoller.class);
        this.defaultSerializer = DefaultSerializer.builder().gson(new Gson()).build();
        this.runtime = LambdaRuntime.builder()
                .lambdaRuntimeInterface(this.runtimeInterface)
                .lambdaInvocationPoller(this.lambdaInvocationPoller)
                .defaultSerializer(this.defaultSerializer)
                .build();
    }

    @Test
    public void postsInitializationErrorWithNoSerialization() throws LambdaRuntimeError {
        final Capture<RequestDeserializer<String>> requestDeserializer = EasyMock.newCapture();
        final Capture<ResponseSerializer<String>> responseSerializer = EasyMock.newCapture();
        final LambdaRuntimeError error = new LambdaRuntimeError("");
        this.lambdaInvocationPoller.pollAndHandleInvocation(
                eq(this.requestHandler),
                capture(requestDeserializer),
                capture(responseSerializer));
        expectLastCall().andThrow(error);
        this.runtimeInterface.postInitializationError(error);
        expectLastCall();
        replayAll();

        this.runtime.initialize(this.requestHandler);
        assertEquals("request", requestDeserializer.getValue().deserialize("request"));
        assertEquals("response", responseSerializer.getValue().serialize("response"));
        verifyAll();
    }

    @Test
    public void postsInitializationErrorWithCustomSerialization() throws LambdaRuntimeError {
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
    public void callsLambdaInvocationPollerWithNoSerialization() throws LambdaRuntimeError {
        final Capture<RequestDeserializer<String>> requestDeserializer = EasyMock.newCapture();
        final Capture<ResponseSerializer<String>> responseSerializer = EasyMock.newCapture();
        this.lambdaInvocationPoller.pollAndHandleInvocation(
                eq(this.requestHandler),
                capture(requestDeserializer),
                capture(responseSerializer));
        expectLastCall().atLeastOnce();
        replayAll();

        tryRunWithTimeout(() -> {
            this.runtime.initialize(this.requestHandler);
        }, 1, TimeUnit.SECONDS);
        assertEquals("request", requestDeserializer.getValue().deserialize("request"));
        assertEquals("response", responseSerializer.getValue().serialize("response"));
        verifyAll();
    }

    @Test
    public void callsLambdaInvocationPollerWithCustomSerialization() throws LambdaRuntimeError {
        this.lambdaInvocationPoller.pollAndHandleInvocation(
                eq(this.requestHandler),
                eq(this.requestDeserializer),
                eq(this.responseSerializer));
        expectLastCall().atLeastOnce();
        replayAll();

        tryRunWithTimeout(() -> {
            this.runtime.initialize(this.requestHandler, this.requestDeserializer, this.responseSerializer);
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

    private void tryRunWithTimeout(ThrowingRunnable runnable, long timeout, TimeUnit unit) {
        runWithTimeout(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                Assert.fail(e.getMessage());
            }
        }, timeout, unit);
    }

    interface ThrowingRunnable {
        void run() throws Exception;
    }
}

package lambda.custom.runtime.java.example.runtime;

import java.util.Optional;
import java.util.ServiceLoader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;

import lambda.custom.runtime.java.example.runtime.serialization.DefaultSerializer;

public class LambdaRuntimeFactory {

    private LambdaRuntimeFactory() {}

    public static LambdaRuntime createLambdaRuntime() {
        final String runtimeApiDomain = Optional.ofNullable(System.getenv("AWS_LAMBDA_RUNTIME_API"))
                .orElseThrow(() -> new RuntimeException("Environment variable 'AWS_LAMBDA_RUNTIME_API' is not set."));
        return createLambdaRuntime(runtimeApiDomain);
    }

    public static LambdaRuntime createLambdaRuntime(String runtimeApiDomain) {
        final String runtimeApiEndpoint = String.format("http://%s", runtimeApiDomain);

        final LambdaRuntimeInterface lambdaRuntimeInterface = LambdaRuntimeInterfaceClient.builder()
                .runtimeApiEndpoint(runtimeApiEndpoint)
                .build();

        final LambdaInvocationPoller lambdaInvocationPoller = LambdaInvocationPoller.builder()
                .lambdaRuntimeInterface(lambdaRuntimeInterface)
                .build();

        // reference: https://immutables.github.io/json.html#type-adapter-registration
        final GsonBuilder gsonBuilder = new GsonBuilder();
        for (TypeAdapterFactory factory : ServiceLoader.load(TypeAdapterFactory.class)) {
            gsonBuilder.registerTypeAdapterFactory(factory);
        }
        final Gson gson = gsonBuilder.create();
        final DefaultSerializer defaultSerializer = DefaultSerializer.builder()
                .gson(gson)
                .build();

        final LambdaRuntime runtime = LambdaRuntime.builder()
                .lambdaRuntimeInterface(lambdaRuntimeInterface)
                .lambdaInvocationPoller(lambdaInvocationPoller)
                .defaultSerializer(defaultSerializer)
                .build();
        return runtime;
    }
}

package lambda.custom.runtime.java.example.runtime;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class LambdaInvocation {

    @NonNull
    private final LambdaContext context;

    @NonNull
    private final String invocationEvent;
}

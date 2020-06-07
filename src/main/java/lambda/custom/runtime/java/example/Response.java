package lambda.custom.runtime.java.example;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public interface Response {
    String getMessage();
}
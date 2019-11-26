package lambda.custom.runtime.java.example.runtime.serialization;

public interface ResponseSerializer<T> {
    String serialize(T response);
}
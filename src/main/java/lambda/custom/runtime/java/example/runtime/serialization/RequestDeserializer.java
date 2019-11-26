package lambda.custom.runtime.java.example.runtime.serialization;

public interface RequestDeserializer<T> {
    T deserialize(String request);
}
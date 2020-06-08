package lambda.custom.runtime.java.example.runtime.serialization;

/**
 * Object that serializes outgoing responses.
 * @param <T> Type of response objects
 */
public interface ResponseSerializer<T> {

    /**
     * Deserializes a response object.
     * @param response response object to serialize
     * @return serialized response
     */
    String serialize(T response);

}
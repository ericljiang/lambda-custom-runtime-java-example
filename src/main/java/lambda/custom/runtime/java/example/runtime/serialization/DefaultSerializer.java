package lambda.custom.runtime.java.example.runtime.serialization;

import com.google.gson.Gson;

import lombok.Builder;
import lombok.NonNull;

@Builder
public class DefaultSerializer {

    @NonNull
    private final Gson gson;

    public <T> String serialize(T value, Class<T> classOfT) {
        return this.gson.toJson(value, classOfT);
    }

    public <T> T deserialize(String string, Class<T> classOfT) {
        return this.gson.fromJson(string, classOfT);
    }

}
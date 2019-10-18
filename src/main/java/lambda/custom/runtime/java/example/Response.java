package lambda.custom.runtime.java.example;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Response {
    private final String message;
}

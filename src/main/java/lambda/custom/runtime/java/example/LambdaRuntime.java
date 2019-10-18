/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package lambda.custom.runtime.java.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LambdaRuntime {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) throws IOException {
        log.info(new LambdaRuntime().getGreeting());
        final URL url = new URL("http://checkip.amazonaws.com");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            final String pageContents = br.lines().collect(Collectors.joining("\n"));
            log.info(pageContents);
        }
    }
}

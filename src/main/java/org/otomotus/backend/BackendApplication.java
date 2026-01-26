package org.otomotus.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Główna klasa aplikacji backendu Otomotus.
 * <p>
 * Odpowiada za inicjalizację i uruchomienie aplikacji Spring Boot z włączoną
 * asynchronicznym przetwarzaniem (@EnableAsync).
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@SpringBootApplication
@EnableAsync
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}

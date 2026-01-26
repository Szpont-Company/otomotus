package org.otomotus.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Serwis do zarządzania przechowywaniem plików obrazów.
 * <p>
 * Odpowiada za zapisywanie zdjęć aukcji na dysku serwera.
 * Pliki są przechowywane w katalogu zdefiniowanym w konfiguracji.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Service
public class ImageStorageService {

    private final Path fileStorageLocation;

    /**
     * Konstruktor inicjalizujący ścieżkę do katalogu przechowywania plików.
     *
     * @param uploadDir ścieżka do katalogu przechowywania (z konfiguracji)
     * @throws RuntimeException jeśli nie udało się utworzyć katalogu
     */
    public ImageStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to create a photo directory.", ex);
        }
    }

    /**
     * Zapisuje plik obrazu na dysku serwera.
     * <p>
     * Plik jest zapisywany z nazwą składającą się z UUID i oryginalnej nazwy.
     * </p>
     *
     * @param file plik do zapisania (MultipartFile)
     * @return nazwa zapisanego pliku
     * @throws RuntimeException jeśli zapisanie pliku się nie powiedzie
     */
    public String storeFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path targetLocation = this.fileStorageLocation.resolve(fileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Error while saving file " + file.getOriginalFilename(), ex);
        }
    }
}
package org.otomotus.backend.config;

/**
 * Enumeracja reprezentująca role użytkowników w systemie.
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
public enum UserRole {
    /** Zwykły użytkownik - może wystawiać aukcje, kupować samochody i komunikować się z innymi */
    USER,
    /** Administrator - ma dostęp do wszystkich funkcji administracyjnych */
    ADMIN
}

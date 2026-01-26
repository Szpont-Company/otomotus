# 🚗 Otomotus — Backend

Backend systemu **Otomotus**, nowoczesnej **platformy aukcyjnej** do sprzedaży **samochodów nowych i używanych**.  
Projekt został zbudowany z użyciem **Spring Boot 3.5.6 (Java 21)** i udostępnia **REST API** wraz z komunikacją **WebSocket** w czasie rzeczywistym.

---

## 🌐 Opis projektu

Otomotus to platforma e-commerce, która łączy **sprzedających i kupujących** pojazdy w jednym miejscu.  
Użytkownicy mogą:
- 🧍‍♂️ Rejestrować konta, logować się oraz weryfikować email (JWT)
- 🚘 Dodawać, edytować, usuwać aukcje samochodów z zdjęciami
- 🔎 Przeglądać aktywne aukcje z paginacją i filtrowaniem
- 📸 Przesyłać wielokrotne zdjęcia do aukcji
- 💬 Komunikować się z innymi użytkownikami przez WebSocket (chat)
- 📄 Generować umowy sprzedaży w formacie PDF
- 📧 Otrzymywać powiadomienia mailowe o nowych wiadomościach
- 🛠️ Administratorzy mogą zarządzać użytkownikami

**Główne funkcjonalności:**
- Autentykacja i autoryzacja za pomocą JWT
- Wsparcie ról użytkowników (USER, ADMIN)
- System aukcji z czasem wygaśnięcia
- Chat w czasie rzeczywistym (WebSocket)
- Zarządzanie zdjęciami aukcji
- Generowanie umów PDF
- Powiadomienia e-mail (asynchroniczne)

---

## 🧱 Stack technologiczny

- **Język:** Java 21  
- **Framework:** Spring Boot 3.5.6
- **Moduły Springa:**
  - Spring Web (REST API)
  - Spring Data JPA (ORM, Hibernate)
  - Spring Security (JWT, role-based access control)
  - Spring Validation (Bean Validation)
  - Spring Mail (asynchroniczne powiadomienia e-mail)
  - Spring WebSocket (komunikacja w czasie rzeczywistym)
- **Baza danych:** MySQL 8
- **ORM:** Hibernate 6
- **Mapowanie:** MapStruct 1.5.5
- **Biblioteki dodatkowe:**
  - Lombok (redukcja boilerplate'u)
  - OpenPDF (generowanie PDF)
  - JWT (io.jsonwebtoken)
- **Budowanie projektu:** Maven 3.9+
- **Dokumentacja kodu:** Javadoc
- **Wersja Java:** Java 21

---

## 🗂 Struktura projektu

```
otomotus/
├── src/
│   ├── main/
│   │   ├── java/org/otomotus/backend/
│   │   │   ├── BackendApplication.java          # Główna klasa aplikacji
│   │   │   ├── auth/                            # Autentykacja i autoryzacja
│   │   │   │   ├── config/                      # JWT, Security Config, validatory
│   │   │   │   ├── controller/                  # AuthController
│   │   │   │   ├── dto/                         # LoginRequestDto, RegisterRequest
│   │   │   │   ├── model/                       # AuthUserDetails
│   │   │   │   └── service/                     # JwtService, CustomUserDetailsService
│   │   │   ├── config/                          # Konfiguracja aplikacji
│   │   │   │   ├── AuctionStatus.java           # Enum statusów aukcji
│   │   │   │   ├── FuelType.java                # Enum typów paliwa
│   │   │   │   ├── TransmissionType.java        # Enum skrzynek biegów
│   │   │   │   ├── UserRole.java                # Enum ról użytkowników
│   │   │   │   ├── WebConfig.java               # CORS, web config
│   │   │   │   └── WebSocketConfig.java         # Konfiguracja WebSocket
│   │   │   ├── controller/                      # REST API Endpointy
│   │   │   │   ├── AuctionController.java       # CRUD aukcji, zdjęcia, umowy
│   │   │   │   ├── UserController.java          # Zarządzanie użytkownikami (admin)
│   │   │   │   └── ChatController.java          # Wiadomości, rozmowy
│   │   │   ├── dto/                             # Data Transfer Objects
│   │   │   │   ├── AuctionCreateRequestDto
│   │   │   │   ├── AuctionUpdateRequestDto
│   │   │   │   ├── AuctionResponseDto
│   │   │   │   ├── UserCreateRequestDto
│   │   │   │   ├── UserResponseDto
│   │   │   │   ├── MessageRequestDto
│   │   │   │   ├── ConversationListDto
│   │   │   │   └── ...
│   │   │   ├── entity/                          # Encje JPA
│   │   │   │   ├── AuctionEntity.java
│   │   │   │   ├── UserEntity.java
│   │   │   │   ├── CarEntity.java
│   │   │   │   ├── MessageEntity.java
│   │   │   │   ├── ConversationEntity.java
│   │   │   │   └── AuctionImageEntity.java
│   │   │   ├── exception/                       # Obsługa wyjątków
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ErrorResponse.java
│   │   │   │   ├── ResourceNotFoundException
│   │   │   │   └── UserAlreadyExistsException
│   │   │   ├── mapper/                          # MapStruct mappery
│   │   │   │   ├── AuctionMapper.java
│   │   │   │   └── UserMapper.java
│   │   │   ├── repository/                      # JPA Repositories
│   │   │   │   ├── AuctionRepository.java
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── MessageRepository.java
│   │   │   │   ├── ConversationRepository.java
│   │   │   │   └── AuctionImageRepository.java
│   │   │   └── service/                         # Logika biznesowa
│   │   │       ├── AuctionService.java
│   │   │       ├── UserService.java
│   │   │       ├── ChatService.java
│   │   │       ├── EmailService.java
│   │   │       ├── ImageStorageService.java
│   │   │       ├── ContractService.java
│   │   │       └── MailNotificationService.java
│   │   └── resources/
│   │       ├── application.properties           # Podstawowa konfiguracja
│   │       ├── application.yml                  # Konfiguracja YAML
│   │       └── docker-compose.yml               # Compose dla MySQL
│   └── test/
│       └── java/org/otomotus/backend/           # Testy (JUnit 5)
├── uploads/                                      # Katalog przechowywania zdjęć
├── pom.xml                                       # Konfiguracja Maven
├── mvnw / mvnw.cmd                              # Maven Wrapper
└── README.md
```

**Pakiety:**
- `auth` - Autentykacja, JWT, Security Config
- `config` - Konfiguracja aplikacji (enumeracje, WebSocket)
- `controller` - REST API endpointy
- `dto` - Transfer Objects (żądania i odpowiedzi)
- `entity` - Encje JPA (mapping do bazy danych)
- `exception` - Obsługa błędów i wyjątków
- `mapper` - Konwersje między encjami a DTO (MapStruct)
- `repository` - Data Access Layer (JPA)
- `service` - Logika biznesowa aplikacji

---

## ⚙️ Konfiguracja środowiska

### Wymagania:
- **Java 21** (JDK)
- **Maven 3.9+** (lub użyj Maven Wrapper: `./mvnw`)
- **MySQL 8.0+**
- **IDE:** IntelliJ IDEA / VS Code / Eclipse

### Plik konfiguracyjny

Utwórz plik `application.yml` w `src/main/resources/`:

```yaml
spring:
  application:
    name: om
  datasource:
    url: jdbc:mysql://localhost:3306/otomotus
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update  # lub: create-drop, validate
    show-sql: false
    database-platform: org.hibernate.dialect.MySQL8Dialect
  mail:
    host: smtp.gmail.com
    port: 587
    username: your_email@gmail.com
    password: your_app_password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true

jwt:
  secret: your_very_long_secret_key_base64_encoded_here_min_256bits
  expiration: 86400000  # 24 godziny w milisekundach

file:
  upload-dir: ./uploads  # Katalog do przechowywania zdjęć

server:
  port: 8080
```

**Ważne:** 
- Tajny klucz JWT powinien być zakodowany w Base64 i mieć minimum 256 bitów
- Dla Gmail użyj [App Password](https://support.google.com/accounts/answer/185833), nie zwykłego hasła

---

## 🚀 Uruchomienie projektu
---

### 1️⃣ Przygotowanie bazy danych

Opcja A: MySQL lokalnie
```bash
mysql -u root -p
CREATE DATABASE otomotus;
```

Opcja B: Docker (rekomendowane)
```bash
docker-compose -f src/main/resources/docker-compose.yml up -d
```

### 2️⃣ Budowanie projektu

```bash
# Z Maven Wrapper (zalecane)
./mvnw clean install

# Lub z systemowym Maven
mvn clean install
```

### 3️⃣ Uruchomienie aplikacji

```bash
# Z Maven
./mvnw spring-boot:run

# Lub bezpośrednio
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

Aplikacja będzie dostępna pod adresem: **http://localhost:8080**

---

## 📚 API Endpoints

### 🔐 Autentykacja (`/api/auth`)
- `POST /api/auth/register` - Rejestracja nowego użytkownika
- `POST /api/auth/login` - Logowanie i uzyskanie JWT
- `GET /api/auth/verify?token=...` - Weryfikacja emaila

### 🚘 Aukcje (`/api/auctions`)
- `GET /api/auctions` - Pobranie wszystkich aktywnych aukcji (z paginacją)
- `GET /api/auctions/{id}` - Szczegóły aukcji
- `POST /api/auctions` - Utworzenie nowej aukcji
- `PUT /api/auctions/{id}` - Aktualizacja aukcji
- `DELETE /api/auctions/{id}` - Usunięcie aukcji
- `GET /api/auctions/my-auctions` - Aukcje zalogowanego użytkownika
- `POST /api/auctions/{id}/images` - Przesłanie zdjęcia do aukcji
- `GET /api/auctions/{id}/contract` - Pobranie umowy PDF

### 👥 Użytkownicy (`/api/users`)
- `GET /api/users` - Lista wszystkich użytkowników (admin)
- `GET /api/users/{userId}` - Szczegóły użytkownika (admin)
- `PATCH /api/users/{userId}` - Aktualizacja użytkownika (admin)
- `DELETE /api/users/{userId}` - Usunięcie użytkownika (admin)

### 💬 Wiadomości (`/api/messages`)
- `POST /api/messages/send` - Wysłanie wiadomości
- `GET /api/messages/conversations/my` - Moje rozmowy
- `GET /api/messages/conversation/{conversationId}/messages` - Wiadomości z rozmowy
- `PATCH /api/messages/{id}` - Edycja wiadomości
- `PATCH /api/messages/{id}/read` - Oznaczenie jako przeczytane
- `PATCH /api/messages/{id}/unread` - Oznaczenie jako nieprzeczytane
- `DELETE /api/messages/{id}` - Usunięcie wiadomości

### 🔗 WebSocket (`/ws`)
Połączenie WebSocket dla komunikacji w czasie rzeczywistym:
```
ws://localhost:8080/ws
```

---

## 🔐 Autoryzacja i JWT

Wszystkie chronione endpointy wymagają nagłówka `Authorization`:

```bash
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
     http://localhost:8080/api/auctions
```

**Token JWT zawiera:**
- `sub` - ID użytkownika
- `iat` - Czas wydania
- `exp` - Czas wygaśnięcia

---

## 📖 Dokumentacja kodu

Projekt zawiera pełną dokumentację Javadoc dla wszystkich klas i metod.

Aby wygenerować HTML dokumentację:
```bash
./mvnw javadoc:javadoc
```

Dokumentacja będzie dostępna w: `target/site/apidocs/index.html`

---

## 🧪 Testowanie

Aby uruchomić testy:
```bash
./mvnw test
```

Aby sprawdzić pokrycie testami:
```bash
./mvnw clean test jacoco:report
```

Raport będzie dostępny w: `target/site/jacoco/index.html`

---

## 🐛 Obsługa błędów

Backend zwraca standaryzowane odpowiedzi błędów:

```json
{
  "timestamp": "24-01-2026 13:30:45",
  "status": 404,
  "error": "Not Found",
  "message": "Auction not found",
  "path": "/api/auctions/invalid-id"
}
```

**Kody statusu HTTP:**
- `200 OK` - Sukces
- `201 Created` - Zasób utworzony
- `400 Bad Request` - Błędy walidacji
- `401 Unauthorized` - Brak/nieważny token JWT
- `403 Forbidden` - Brak uprawnień
- `404 Not Found` - Zasób nie znaleziony
- `409 Conflict` - Konflikt (np. email już istnieje)
- `500 Internal Server Error` - Błąd serwera

---

## 🔧 Tryb development

Dla development'u rekomendujemy:
1. Zainstalować [Spring Boot DevTools](https://spring.io/projects/spring-boot#learn)
2. Włączyć hot-reload w IDE
3. Ustawić `ddl-auto: create-drop` dla testów

---

## 📦 Zależności Maven

Główne zależności projektu:
- Spring Boot 3.5.6
- Spring Security + JWT (io.jsonwebtoken)
- Hibernate + JPA
- MapStruct 1.5.5
- Lombok
- OpenPDF (generowanie PDF)
- MySQL Connector 8.0+

Pełna lista w pliku `pom.xml`

---

## 🚀 Wdrożenie (Deployment)

### Budowanie JAR
```bash
./mvnw clean package
```

### Uruchomienie JAR
```bash
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### Docker (opcjonalnie)
```dockerfile
FROM openjdk:21-slim
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 📝 Konwencje kodowania

Projekt następuje standardy:
- **Java Naming Conventions** - camelCase dla zmiennych, PascalCase dla klas
- **Spring Best Practices** - warstwowa architektura (controller → service → repository)
- **REST Best Practices** - RESTful endpoints, standardowe kody HTTP
- **Javadoc** - dokumentacja dla wszystkich public klasy i metod

---

## 👥 Autorzy

Zespół Projektu Otomotus

---

## 📄 Licencja

Projekt jest objęty licencją zawartą w pliku LICENSE.txt


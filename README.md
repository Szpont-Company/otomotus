# 🚗 Otomotus — Backend

Backend systemu **Otomotus**, nowoczesnej **platformy ogłoszeniowej** do sprzedaży i kupna **samochodów nowych i używanych**.  
Projekt został zbudowany z użyciem **Spring Boot (Java)** i udostępnia **REST API** dla aplikacji frontendowej oraz panelu administracyjnego.

---

## 🌐 Opis projektu

Otomotus to platforma, która łączy **sprzedających i kupujących** pojazdy w jednym miejscu.  
Użytkownicy mogą:
- 🧍‍♂️ Rejestrować konta i logować się za pomocą JWT
- 🚘 Dodawać, edytować i usuwać ogłoszenia samochodów
- 🔎 Wyszukiwać oferty po marce, modelu, roczniku, cenie, lokalizacji itp.
- ❤️ Dodawać ogłoszenia do ulubionych
- 💬 Kontaktować się ze sprzedającymi
- 🛠 Administratorzy mogą zarządzać użytkownikami i treściami ogłoszeń

Backend odpowiada za:
- Logikę biznesową i komunikację z bazą danych
- Uwierzytelnianie i autoryzację użytkowników (JWT)
- Przechowywanie danych o ofertach i użytkownikach
- Obsługę filtrów, sortowania i paginacji wyników wyszukiwania
- Wysyłanie powiadomień e-mail (np. potwierdzenia rejestracji, kontakt z ogłoszeniodawcą)

---

## 🧱 Stack technologiczny

- **Język:** Java 25  
- **Framework:** Spring Boot 3.x  
- **Moduły Springa:**
  - Spring Web (REST API)
  - Spring Data JPA (ORM)
  - Spring Security (JWT Auth)
  - Spring Validation
  - Spring Mail (powiadomienia e-mail)
- **Baza danych:** MySQL 8
- **ORM:** Hibernate
- **Narzędzia:** Lombok, MapStruct / ModelMapper
- **Budowanie projektu:** Maven  
- **Testy:** JUnit 5, Mockito
- **Dokumentacja API:** Swagger / Springdoc OpenAPI

---

## 🗂 Struktura projektu
```
  otomotus-backend/
  ├── src/
  │ ├── main/
  │ │ ├── java/com/otomotus/
  │ │ │ ├── controller/ # Endpointy REST
  │ │ │ ├── service/ # Logika biznesowa
  │ │ │ ├── repository/ # Warstwa dostępu do danych (JPA)
  │ │ │ ├── model/ # Encje i DTO
  │ │ │ ├── security/ # Konfiguracja JWT, filtry, role
  │ │ │ └── OtomotusApp.java # Klasa główna aplikacji
  │ │ └── resources/
  │ │ ├── application.yml # Konfiguracja środowisk
  │ │ └── static/ # Pliki statyczne (opcjonalne)
  │ └── test/
  │ └── java/com/otomotus/ # Testy jednostkowe i integracyjne
  ├── pom.xml
  └── README.md
```

---

## ⚙️ Konfiguracja środowiska

### Wymagania:
- Java 25 (JDK)
- Maven 3.9+
- MySQL 8+
- IDE: IntelliJ IDEA / VS Code / Eclipse

### Plik konfiguracyjny
Skopiuj plik `application-example.yml` do `application.yml` i uzupełnij dane:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/otomotus
    username: root
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
security:
  jwt:
    secret: your_secret_key
    expiration: 86400000
```

---

## 🚀 Uruchomienie projektu

1️⃣ Utwórz bazę danych MySQL
```
mysql -u root -p
CREATE DATABASE otomotus;
```

2️⃣ Zbuduj projekt
```
mvn clean install
```

3️⃣ Uruchom aplikację
```
mvn spring-boot:run
```

Aplikacja wystartuje pod adresem:
👉 http://localhost:8080

---

## 🔐 Autoryzacja i API

System korzysta z JWT (JSON Web Token) dla uwierzytelniania.

- Endpoint logowania: POST /api/auth/login
- Token JWT przesyłany w nagłówku:
```
Authorization: Bearer <token>
```
### Dokumentacja API (Swagger)

Po uruchomieniu aplikacji:
👉 http://localhost:8080/swagger-ui/index.html

---

## 🧪 Testowanie

Aby uruchomić testy jednostkowe i integracyjne:
```
mvn test
```

---

### 👥 Autorzy

Zespół Projektu Otomotus

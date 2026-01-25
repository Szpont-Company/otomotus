================================================================================
                    DOKUMENTACJA SYSTEMU OTOMOTUS
                     Indeks i Instrukcja Użycia
================================================================================

Data utworzenia: 25.01.2026
Wersja: 1.0
Autorzy: Otomotus Development Team

================================================================================
SPIS PLIKÓW DOKUMENTACJI
================================================================================

1. README.txt (ten plik)
   - Indeks dokumentacji
   - Instrukcje generowania diagramów
   - Informacje ogólne

2. 01_Opis_Systemu.txt
   - Cel systemu i zakres funkcjonalny
   - Definicje grup użytkowników
   - Model biznesowy
   - Architektura i technologie

3. 02_Use_Case_Diagram.puml (PlantUML)
   - Diagram przypadków użycia
   - Aktorzy: Guest, User, Seller, Admin
   - 39 przypadków użycia

4. 03_Sequence_Diagram_Utworzenie_Aukcji.puml (PlantUML)
   - Diagram sekwencji: tworzenie aukcji
   - Przepływ od logowania do publikacji
   - 5 faz procesu

5. 04_Sequence_Diagram_Komunikacja.puml (PlantUML)
   - Diagram sekwencji: komunikacja WebSocket
   - Przepływ wiadomości między użytkownikami
   - Real-time messaging

6. 05_Opis_Diagramow_Sequence.txt
   - Szczegółowy opis diagramów sekwencji
   - Wyjaśnienie każdej fazy
   - Mechanizmy bezpieczeństwa
   - Obsługa błędów

7. 06_Class_Diagram_Entities.puml (PlantUML)
   - Diagram klas biznesowych (encji)
   - 8 głównych encji
   - Relacje i atrybuty

8. 07_Component_Diagram.puml (PlantUML)
   - Diagram komponentów architektury
   - Warstwy systemu
   - Przepływ danych

9. 08_Opis_Diagramow_Klas_Komponentow.txt
   - Szczegółowy opis klas i komponentów
   - Wyjaśnienie relacji
   - Strategie ładowania
   - Separacja odpowiedzialności

10. 09_MoSCoW_Priorytetyzacja.txt
    - Systematyzacja funkcji metodą MoSCoW
    - Must/Should/Could/Won't Have
    - Status implementacji
    - Rekomendacje rozwoju

================================================================================
JAK GENEROWAĆ DIAGRAMY Z PLIKÓW .puml
================================================================================

Pliki .puml to skrypty w języku PlantUML, które generują diagramy UML.

OPCJA 1: ONLINE (Najszybsza)
-----------------------------
1. Otwórz: http://www.plantuml.com/plantuml/uml/
2. Skopiuj zawartość pliku .puml
3. Wklej do edytora online
4. Diagram wygeneruje się automatycznie
5. Pobierz jako PNG/SVG/PDF

OPCJA 2: VS CODE (Zalecana dla programistów)
---------------------------------------------
1. Zainstaluj VS Code
2. Zainstaluj rozszerzenie: "PlantUML" by jebbs
3. Zainstaluj Graphviz: https://graphviz.org/download/
4. Otwórz plik .puml w VS Code
5. Naciśnij Alt+D aby zobaczyć podgląd
6. Prawy przycisk → "Export Current Diagram"

OPCJA 3: INTELLIJ IDEA (Dla użytkowników JetBrains)
----------------------------------------------------
1. Zainstaluj plugin "PlantUML integration"
2. Otwórz plik .puml
3. Podgląd pojawi się automatycznie
4. Prawy przycisk → "Export to PNG/SVG"

OPCJA 4: COMMAND LINE (Dla zaawansowanych)
-------------------------------------------
1. Zainstaluj Java
2. Pobierz plantuml.jar: https://plantuml.com/download
3. Uruchom:
   java -jar plantuml.jar plik.puml
4. Diagram zapisze się jako plik.png

OPCJA 5: DOCKER (Jeśli masz Docker)
------------------------------------
docker run --rm -v $(pwd):/data plantuml/plantuml -tpng /data/*.puml

================================================================================
STRUKTURA DIAGRAMÓW
================================================================================

Use Case Diagram (02_Use_Case_Diagram.puml):
---------------------------------------------
Pokazuje:
• 4 aktorów (Guest, User, Seller, Admin)
• 39 przypadków użycia pogrupowanych w pakiety:
  - Funkcje Publiczne
  - Autentykacja
  - Zarządzanie Profilem
  - Ulubione Aukcje
  - Komunikacja
  - Opinie i Oceny
  - Zarządzanie Aukcjami
  - Dokumenty
  - Panel Administracyjny
• Relacje: uses, includes, extends
• Dziedziczenie między aktorami

Sequence Diagram - Utworzenie Aukcji (03_...):
-----------------------------------------------
Pokazuje przepływ:
1. Faza autoryzacji (login → JWT token)
2. Faza przygotowania danych (formularz)
3. Faza upload zdjęć (multipart files)
4. Faza utworzenia aukcji (POST request)
5. Faza weryfikacji (GET details)

Komponenty:
• Sprzedawca (aktor)
• Frontend, API Gateway, Controllers, Services, Repositories
• Database
• External systems

Sequence Diagram - Komunikacja (04_...):
-----------------------------------------
Pokazuje przepływ:
1. Inicjalizacja połączenia WebSocket
2. Wysłanie pierwszej wiadomości
3. Broadcast przez WebSocket
4. Odpowiedź sprzedawcy
5. Oznaczenie jako przeczytane
6. Historia konwersacji

Specyfika:
• Real-time communication
• WebSocket Server jako broker
• Dwukierunkowa komunikacja
• Offline message handling

Class Diagram (06_Class_Diagram_Entities.puml):
------------------------------------------------
Pokazuje:
• 8 głównych encji (User, Auction, Car, Image, Review, Conversation, Message)
• 5 enumów (UserRole, AuctionStatus, FuelType, TransmissionType)
• Relacje:
  - OneToMany / ManyToOne
  - ManyToMany (Favorites)
  - OneToOne (Auction-Car)
• Atrybuty i metody każdej klasy

Component Diagram (07_Component_Diagram.puml):
-----------------------------------------------
Pokazuje architekturę warstwową:
• Frontend Layer (Browser, SPA, WebSocket Client)
• API Gateway (REST, JWT Filter)
• Controller Layer (5 kontrolerów)
• Service Layer (8 serwisów)
• Repository Layer (7 repozytoriów)
• Data Layer (Database)
• External Systems (SMTP, Storage, PDF)
• Real-time (WebSocket Server)

================================================================================
KONWENCJE UŻYTE W DOKUMENTACJI
================================================================================

Oznaczenia statusu:
✓ - Zaimplementowane
○ - Do zaimplementowania
✗ - Świadomie pominięte

Priorytety (MoSCoW):
M - Must Have (krytyczne)
S - Should Have (ważne)
C - Could Have (pożądane)
W - Won't Have (pominięte)

Notacja klas:
- prywatne
+ publiczne
# protected
~ package-private
<<stereotyp>>

Relacje:
→ - asocjacja (kierunkowa)
↔ - asocjacja (dwukierunkowa)
*-- - kompozycja
o-- - agregacja
<|-- - dziedziczenie
..> - zależność

================================================================================
KONTEKST BIZNESOWY
================================================================================

System: Otomotus
Typ: Platforma aukcyjna do sprzedaży samochodów
Rynek: Polska
Model: Marketplace (B2C, C2C)

Główne funkcje:
• Ogłoszenia sprzedaży pojazdów
• Komunikacja kupujący-sprzedawca
• System opinii i reputacji
• Zarządzanie użytkownikami
• Administracja platformą

Technologie:
• Backend: Java 17, Spring Boot 3.x
• Security: JWT, BCrypt
• Database: PostgreSQL/MySQL
• Real-time: WebSocket (STOMP)
• API: RESTful
• Dokumenty: PDF generation

Użytkownicy:
1. Guest (niezalogowany) - przeglądanie
2. User (kupujący) - przeglądanie, ulubione, chat, opinie
3. Seller (sprzedawca) - wszystko User + tworzenie aukcji
4. Admin (administrator) - moderacja, zarządzanie

================================================================================
STANDARDY I BEST PRACTICES
================================================================================

Architektura:
• Layered Architecture (Prezentacja → Logika → Dane)
• Separation of Concerns
• SOLID Principles
• RESTful API design

Bezpieczeństwo:
• JWT token authentication
• BCrypt password hashing
• Role-based access control (RBAC)
• HTTPS only (produkcja)
• CORS configuration
• Input validation

Baza danych:
• Normalized schema (3NF)
• Foreign keys + constraints
• Indexes for performance
• Cascade operations
• Soft delete (opcjonalnie)

Kod:
• Clean Code principles
• JavaDoc comments
• Bean Validation
• Exception handling
• Transaction management (@Transactional)
• DTO pattern (Entity ↔ DTO separation)

================================================================================
SŁOWNIK POJĘĆ
================================================================================

Auction - Aukcja, ogłoszenie sprzedaży samochodu
Car - Samochód, szczegóły techniczne pojazdu
Seller - Sprzedawca, użytkownik wystawiający aukcję
Buyer - Kupujący, użytkownik zainteresowany zakupem
Favorite - Ulubione, zapisana aukcja
Review - Opinia, ocena sprzedawcy
Rating - Ocena numeryczna (1-5 gwiazdek)
Conversation - Konwersacja, rozmowa między 2 użytkownikami
Message - Wiadomość w konwersacji
VIN - Vehicle Identification Number, numer identyfikacyjny pojazdu
JWT - JSON Web Token, token autoryzacyjny
DTO - Data Transfer Object, obiekt transferu danych
Entity - Encja, obiekt domenowy mapowany na tabelę DB
Repository - Repozytorium, warstwa dostępu do danych
Service - Serwis, warstwa logiki biznesowej
Controller - Kontroler, warstwa obsługi HTTP
CRUD - Create, Read, Update, Delete
REST - Representational State Transfer
WebSocket - Protokół dwukierunkowej komunikacji
STOMP - Simple Text Oriented Messaging Protocol
Pageable - Obiekt paginacji (Spring Data)
Cascade - Operacje kaskadowe (usuwanie, aktualizacja)
Lazy/Eager - Strategie ładowania relacji

================================================================================
INFORMACJE O WSPARCIU
================================================================================

W przypadku pytań lub problemów:
• Sprawdź szczegółowe opisy w plikach .txt
• Wygeneruj diagramy z plików .puml
• Dokumentacja Spring Boot: https://spring.io/projects/spring-boot
• Dokumentacja PlantUML: https://plantuml.com/

System został zaprojektowany zgodnie z najlepszymi praktykami
inżynierii oprogramowania i jest gotowy do wdrożenia produkcyjnego.

================================================================================
CHANGELOG
================================================================================

v1.0 - 2026-01-25
- Utworzenie pełnej dokumentacji
- Wszystkie diagramy UML
- Opisy szczegółowe
- Priorytetyzacja MoSCoW
- MVP ukończone

================================================================================
LICENCJA
================================================================================

Dokumentacja systemu Otomotus.
© 2026 Otomotus Development Team
Wszelkie prawa zastrzeżone.

================================================================================
KONIEC DOKUMENTU
================================================================================

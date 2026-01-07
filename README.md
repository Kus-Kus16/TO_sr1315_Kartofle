# Board Game Gather

Aplikacja służy do organizowania sesji gier planszowych. Umożliwia zarządzanie katalogiem gier, tworzenie sesji rozgrywek oraz dołączanie do nich przez innych użytkowników.

---

## Autorzy

Maciej Kus
Kamil Życzkowski
Mariusz Krause

## Technologie
 - Backend: Java 25, Spring Boot
 - Frontend: React, Typescript
 - Komunikacja: REST API
 - Baza danych: H2

## Milestone 1
### Model bazodanowy
![](img/m1/dbmodel.png)

### Zrealizowane funkcjonalności
 - Rejestracja/logowanie użytkowników (login)
 - Listowanie gier planszowych z katalogu
 - Wyświetlanie szczegółów gry
 - Dodawanie nowych gier planszowych do katalogu
 - Listowanie istniejących sesji wraz ze szczegółami
 - Dołączanie do istniejącej sesji
 - Tworzenie nowych sesji z wybraną planszówką

### Endpointy
![](img/m1/endpoints.png)

## Uruchomienie projektu
### Backend
 W katalogu głównym projektu:
 ```
 cd ./backend
 ```
 Tworzymy plik `.env` na podstawie `.env.example`, z danymi do logowania do bazy danych. Następnie:
 ```
 docker-compose up
 ```
 Uruchamiamy aplikację SpringBoot (np. przez IntelliJ IDEA), dodając plik `.env` jako zmienne środowiskowe, oraz profil `dev` w konfiguracji uruchomieniowej.
### Frontend
 W katalogu głównym projektu:
 ```
 cd ./frontend

 npm install

 npm run dev
 ```

 Strona jest dostępna pod adresem http://localhost:5173/
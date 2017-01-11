# Patronage-entry-task

Task for Patronage 2017 entry evaluation - rest api.

Dokumentacja PL:

Restowe api wypożyczalni filmów.

ZADANIE 2: (nowe)

Dostęp do api zabezpieczony jest autoryzacją typu Basic Auth. Wymagane jest podanie loginu i hasła w headerze wraz
z wysyłanym zapytaniem. Przykładowi użytkownicy zdefiniowani są w pliku WebSecurityConfig (inMemoryUserDetailsManager).

Występują dwa typy użytkowników: USER - użytkownik zwykły, ADMIN - administrator. Użytkownicy zwykli mają dostęp do pobierania
informacji o filmach, aktorach, wypożyczonych filmach, informacji o swoim profilu; mogą wypożyczać oraz zwracać filmy.
Podaczas tworzenia nowego użytkownika autentykacja nie jest wymagana (z wyjątkiem tworzenia użytkownika typu ADMIN, do 
czego mają dostęp tylko admini).
Do pozostałych operacji mają dostęp tylko użytkownicy o roli ADMIN.

List dostępnych operacji:

USER:

GET: <app_url>/user

    zwraca informacje o profilu aktualnie zalogowanego usera
    
GET: <app_url>/admin/user/all

    Zwraca listę wszystkich użytkowników aplikacji
    
POST: <app_url>/user/create
    
    Tworzy nowego użytkownika typu USER o podanych parametrach przesłanych w headerze:
        login - nazwa użytkownika (unikalna)
        password - hasło do konta

POST: <app_url>/admin/user/createAdmin

    Tworzy nowego użytkownika typu ADMIN o podanych parametrach przesłanych w headerze:
        login - nazwa użytkownika (unikalna)
        password - hasło do konta
        
POST: <app_url>/user/rent

    Wypożycza filmy o podanych w parametrze zapytania id. Można podać wiele id filmów.
    Parametry:
        movieId - id wybranego filmu, np:
            [..]/user/rent?movieId=1&movieId=6&movieId=13

GET: <app_url>/user/movies

    Zwraca listę filmów aktualnie wypożyczonych przez użytkownika
    
GET: <app_url>/user/movies/{userId}

    Zwraca listę filmów aktualnie wypożyczonych przez użytkownika o podanym id
    
POST: <app_url>/user/return

    Zwraca filmy o podanych w parametrze zapytania id. Można podać wiele id filmów.
    Parametry:
        movieId - id wybranego filmu, np:
            [..]/user/return?movieId=11&movieId=2


FILMY:

GET: <app_url>/movies/available

    Zwraca listę wszystkich filmów aktualnie możliwych do wypożyczenia
    
GET: <app_url>/movies/newest

    Zwraca listę wszystkich filmów aktualnie możliwych do wypożyczenia w kategorii "nowości"

GET: <app_url>/movies/hits

    Zwraca listę wszystkich filmów aktualnie możliwych do wypożyczenia w kategorii "hity"

GET: <app_url>/movies/other

    Zwraca listę wszystkich filmów aktualnie możliwych do wypożyczenia w kategorii "pozostałe"

.

.

ZADANIE 1: (stare)

FILMY:


GET: <app_url>/admin/movies

    zwraca wszystkie filmy w bazie danych

GET: <app_url>/movies/{id}

    zwraca film o podanym id

PUT: <app_url>/admin/movies/insert

    tworzy nowy film
    parametry zapytania:
        title - tytuł filmu
        desc - opis filmu
        type - typ filmu (0 - nowości, 1 - hity, 2 - pozostałe)
        price - cena filmu

DELETE: <app_url>/admin/movies/remove/{id}

    usuwa z bazy danych film o podanym id

POST: <app_url>/admin/movies/update/{id}

    modyfikuje film w bazie danych, można podać jeden z dwóch, lub obydwa parametry
    parametry zapytania:
        title - nowy tytuł filmu
        desc - nowy opis filmu
        type - nowy typ filmu (0 - nowości, 1 - hity, 2 - pozostałe)
        price - nowa cena filmu

GET: <app_url>/movies/{id}/actors

    zwraca aktorów grających w filmie o podanym id

PUT: <app_url>/admin/movies/{id}/addActor/{actorId}

    dodaje aktora o podanym actorId do filmu o podanym id

DELETE: <app_url>/admin/movies/{id}/removeActor/{actorId}

    usuwa z obsady filmu o podanym id aktora o podanym actorId




AKTORZY:


GET: <app_url>/actors

    zwraca wszystkich aktorów w bazie danych

GET: <app_url>/actors/{id}

    zwraca aktora o podanym id

PUT: <app_url>/admin/actors/insert

    tworzy nowego aktora
    parametry zapytania:
        name - imię/nazwisko aktora

DELETE: <app_url>/admin/actors/remove/{id}

    usuwa z bazy danych aktora o podanym id

POST: <app_url>/admin/actors/update/{id}

    modyfikuje aktora w bazie danych
    parametry zapytania:
        name - nowe imię/nazwisko aktora
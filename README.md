# Patronage-entry-task1

First task for Patronage 2017 entry evaluation - rest api.

Dokumentacja PL:

Restowe api bazy filmów. Api udostępnia następujące operacje dla aktorów i filmów:


        FILMY:

GET: <app_url>/movies

    zwraca wszystkie filmy w bazie danych

GET: <app_url>/movies/{id}

    zwraca film o podanym id

PUT: <app_url>/movies/insert

    tworzy nowy film
    parametry zapytania:
        title - tytuł filmu
        desc - opis filmu

DELETE: <app_url>/movies/remove/{id}

    usuwa z bazy danych film o podanym id

POST: <app_url>/movies/update/{id}

    modyfikuje film w bazie danych, można podać jeden z dwóch, lub obydwa parametry
    parametry zapytania:
        title - nowy tytuł filmu
        desc - nowy opis filmu

GET: <app_url>/movies/{id}/actors

    zwraca aktorów grających w filmie o podanym id

PUT: <app_url>/movies/{id}/addActor/{actorId}

    dodaje aktora o podanym actorId do filmu o podanym id

DELETE: <app_url>/movies/{id}/removeActor/{actorId}

    usuwa z obsady filmu o podanym id aktora o podanym actorId




        AKTORZY:

GET: <app_url>/actors

    zwraca wszystkich aktorów w bazie danych

GET: <app_url>/actors/{id}

    zwraca aktora o podanym id

PUT: <app_url>/actors/insert

    tworzy nowego aktora
    parametry zapytania:
        name - imię/nazwisko aktora

DELETE: <app_url>/actors/remove/{id}

    usuwa z bazy danych aktora o podanym id

POST: <app_url>/actors/update/{id}

    modyfikuje aktora w bazie danych
    parametry zapytania:
        name - nowe imię/nazwisko aktora
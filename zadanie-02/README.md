# Task no. 2
!! Ngrok is implemented with docker-compose.

## Dockerhub
You can find the docker image [here](https://hub.docker.com/repository/docker/n033/uj-android-zad-02).

## Running using dockerfile (without ngrok), in `zadanie-02` directory:
1. `docker build -t "zad-02" .`
2. `docker run "zad-02"`
  
## Running by image pulling
1. `docker pull n033/uj-android-zad-02:1.1`
2. `docker run "n033/uj-android-zad-02:1.1"`

## Running with docker compose (with ngrok)
In the directory where `compose.yaml` file is located, run:
`docker compose up`
If you want to access the app through ngrok, after running docker-compose:
1. Go to `localhost:4040`
2. Get the link from the webpage


# Requirements

✅ 3.0 Należy stworzyć model Produktów z minimum czterema polami oraz metody w kontrolerze odpowiedzialne za odczyt jednego oraz wielu produktów; metody powinny zwracać JSONa; należy stworzyć odpowiednią tablicę routingu; dane powinny być zapisane w bazie danych, np. SQLite

✅ 3.5 Należy stworzyć dwa modele, np. Produkty oraz Kategorie, z jedną relacją oraz minimum czterema polami

✅ 4.0 Należy stworzyć metody CRUD po REST dla modeli z oceny 3.5

✅ 4.5 Należy aplikację uruchomić na dockerze (stworzyć obraz) oraz dodać Ngrok

✅ 5.0 Należy dodać konfigurację CORS dla dwóch hostów dla metod CRUD

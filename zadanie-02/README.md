# Task no. 2
!! Ngrok is implemented with docker-compose.

## Dockerhub
You can find the docker image [here](https://hub.docker.com/repository/docker/n033/uj-android-zad-02).

## Running using dockerfile (without ngrok), in `zadanie-02` directory:
1. `docker build -t "zad-01" .`
2. `docker run "zad-01"
  
## Running with image pulling
1. `docker pull n033/uj-android-zad-02:1.1`
2. `docker run "n033/uj-android-zad-02:1.1`

## Running with docker compose (with ngrok)
In the directory where `compose.yaml` file is located, run:
`docker compose up`
If you want to access the app through ngrok, after running docker-compose:
1. Go to `localhost:4040`
2. Get the link from the webpage



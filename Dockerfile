FROM ubuntu:22.04

ENV TZ="Europe/Warsaw" \
    PYTHON_VERSION="3.8" \
    KOTLIN_VERSION="1.7.20" \
    GRADLE_VERSION="7.5.1" 

ENV KOTLIN_URL="https://github.com/JetBrains/kotlin/releases/download/v${KOTLIN_VERSION}/kotlin-compiler-${KOTLIN_VERSION}.zip" \
    GRADLE_URL="https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip"

RUN apt-get update
RUN apt install wget -y && \
    apt install software-properties-common -y && \
    add-apt-repository ppa:deadsnakes/ppa -y && \
    apt install unzip -y && \
    apt install tzdata -y

RUN apt-get install -y "python${PYTHON_VERSION}"
RUN apt-get install -y openjdk-8-jdk 
RUN cd /usr/lib && \
    wget $KOTLIN_URL && \
    unzip kotlin-compiler-${KOTLIN_VERSION}.zip && \
    rm kotlin-compiler-${KOTLIN_VERSION}.zip && \
    rm -f kotlinc/bin/*.bat

ENV PATH $PATH:/usr/lib/kotlinc/bin

RUN cd /usr/lib && \
    wget $GRADLE_URL && \
    unzip -d gradle/ gradle-*-bin.zip && \
    rm gradle-*-bin.zip 
ENV PATH=$PATH:/usr/lib/gradle/gradle-${GRADLE_VERSION}/bin

WORKDIR /code

COPY ./src /code

CMD ["./gradlew", "run"]



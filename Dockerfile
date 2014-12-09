FROM dockerfile/java:oracle-java7

RUN add-apt-repository ppa:cwchien/gradle
RUN apt-get update
RUN apt-get install -y gradle

COPY . /code

WORKDIR /code

RUN ["gradle", "build"]

EXPOSE 8080
CMD ["java", "-jar", "build/libs/pawn-boot-0.1.0.jar"]

FROM java:7

RUN apt-get update
RUN wget https://services.gradle.org/distributions/gradle-1.8-all.zip
RUN unzip -o gradle-1.8-all.zip -d /opt/
ENV PATH /opt/gradle-1.8/bin:$PATH

COPY . /code

WORKDIR /code

RUN ["gradle", "build"]

EXPOSE 8080
CMD ["java", "-jar", "build/libs/pawn-boot-0.1.0.jar"]

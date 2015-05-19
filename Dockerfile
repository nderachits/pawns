FROM java:7

COPY jar /code

WORKDIR /code

EXPOSE 8080
CMD ["java", "-jar", "pawn-boot-0.1.0.jar"]

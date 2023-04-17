FROM amazoncorretto:17-alpine-jdk
LABEL description="Echo Hydra Api-gateway Application"
EXPOSE 8080
COPY ./build/libs/oauth-server-0.0.1-SNAPSHOT.jar /opt/oauth-server-0.0.1-SNAPSHOT.jar
WORKDIR /opt
ENTRYPOINT ["java","-jar","oauth-server-0.0.1-SNAPSHOT.jar"]

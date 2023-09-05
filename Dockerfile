FROM openjdk:17-alpine

WORKDIR ./app

ARG VERSION

COPY application/target/application-${VERSION}-jar-with-dependencies.jar ./

CMD ["java", "-jar", "application-${VERSION}-jar-with-dependencies.jar"]

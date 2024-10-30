FROM gradle:8.5-jdk21 AS build

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY src ./src
COPY gradle/libs.versions.toml ./gradle/libs.versions.toml

RUN gradle --no-daemon clean assemble
RUN cp /app/build/libs/*.jar .

FROM azul/zulu-openjdk:21-jre-crac

WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=production

COPY --from=build /app/*.jar application.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar application.jar"]
FROM gradle:8.7.0-jdk21-jammy as build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle clean build -x test

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
RUN addgroup -S passwordtest && adduser -S passwordtest -G passwordtest
USER passwordtest:passwordtest
COPY --from=build /app/build/libs/passwords-0.0.1-SNAPSHOT.jar ./
CMD ["java", "-jar", "passwords-0.0.1-SNAPSHOT.jar"]
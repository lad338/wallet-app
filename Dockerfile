FROM gradle:jdk11-openj9 as gradle-cache
WORKDIR /builder
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME /home/gradle/cache_home
COPY build.gradle .
RUN gradle clean

FROM gradle:jdk11-openj9 as builder
COPY --from=gradle-cache /home/gradle/cache_home /home/gradle/.gradle
WORKDIR /builder
COPY build.gradle .
COPY . .
RUN gradle build


FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /builder/build/libs/wallet-app-0.0.1-SNAPSHOT.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

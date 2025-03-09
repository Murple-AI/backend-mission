FROM openjdk:21-jdk-slim

WORKDIR /app

COPY gradlew build.gradle.kts settings.gradle.kts /app/
COPY gradle/wrapper/gradle-wrapper.jar gradle/wrapper/gradle-wrapper.properties /app/gradle/wrapper/

RUN chmod +x /app/gradlew

COPY . /app

RUN /app/gradlew bootJar --no-daemon --stacktrace

RUN ls -lah /app/build/libs/

EXPOSE 8080

CMD ["sh", "-c", "java -jar /app/build/libs/*.jar"]

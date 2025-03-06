FROM amazoncorretto:21-alpine AS builder

WORKDIR /app
COPY . .
RUN ./gradlew clean build

FROM amazoncorretto:21-alpine AS runner

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar chat-server.jar

EXPOSE 8080

ENTRYPOINT java -jar chat-server.jar 1 > /dev/stdout 2>&1


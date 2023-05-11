FROM openjdk:8
RUN mkdir -p /app
COPY target/todo-app-1.0.0.jar /app/todo-app.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT [ "-jar", "todo-app.jar" ]

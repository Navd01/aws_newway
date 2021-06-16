FROM java:8
VOLUME /tmp
ADD microservice-handler/target/microservice-handler-0.0.1-SNAPSHOT.jar microservice-handler.jar
EXPOSE 8100
RUN bash -c 'touch /app.jar'
ENTRYPOINT [ "java","-Djava.security.egd=file:/dev/./urandom","-jar","/microservice-handler.jar" ]
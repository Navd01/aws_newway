FROM java:8
VOLUME /tmp
ADD stock-service/target/stock-service-0.0.1-SNAPSHOT.jar stock-service.jar
EXPOSE 8082
RUN bash -c 'touch /app.jar'
ENTRYPOINT [ "java","-Djava.security.egd=file:/dev/./urandom","-jar","/stock-service.jar" ]
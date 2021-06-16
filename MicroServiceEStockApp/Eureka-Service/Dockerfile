FROM java:8
VOLUME /tmp
ADD eureka-service/target/eureka-service-0.0.1-SNAPSHOT.jar eureka-service.jar
EXPOSE 8761
RUN bash -c 'touch /app.jar'
ENTRYPOINT [ "java","-Djava.security.egd=file:/dev/./urandom","-jar","/eureka-service.jar" ]
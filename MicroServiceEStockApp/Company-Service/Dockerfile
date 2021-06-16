FROM java:8
VOLUME /tmp
ADD company-service/target/company-service-0.0.1-SNAPSHOT.jar company-service.jar
EXPOSE 8081
RUN bash -c 'touch /app.jar'
ENTRYPOINT [ "java","-Djava.security.egd=file:/dev/./urandom","-jar","/company-service.jar" ]
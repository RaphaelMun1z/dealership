FROM openjdk:21
VOLUME /tmp
ADD target/dealership-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8889
RUN bash -c 'touch /app.jar'
ENTRYPOINT [ "java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar" ]
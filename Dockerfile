FROM openjdk:8

USER root
WORKDIR /app
COPY target/delay-job-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 1989
EXPOSE 1990

CMD java -Xms2g -Xmx2g -Xmn1g -Xss1m -XX:+UseG1GC -jar app.jar
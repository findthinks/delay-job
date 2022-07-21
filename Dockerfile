FROM openjdk:8

USER root
WORKDIR /app
COPY target/delay-job-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 1989
EXPOSE 1990

CMD java -Xms1g -Xmx1g -Xmn512m -Xss1m -XX:+UseG1GC -jar app.jar
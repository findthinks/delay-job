FROM openjdk:8

USER root
WORKDIR /app
COPY target/delay-job.jar app.jar

EXPOSE 8000
EXPOSE 8080

CMD java -Xms1g -Xmx1g -Xmn512m -Xss512k -XX:+UseG1GC -jar app.jar
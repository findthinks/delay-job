FROM openjdk:8

USER root
WORKDIR /app
COPY target/delay-job.jar app.jar

EXPOSE 8000
EXPOSE 8080

CMD java -Xms2g -Xmx2g -Xmn1g -Xss1m -XX:+UseG1GC -jar app.jar
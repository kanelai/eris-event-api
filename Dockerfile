FROM openjdk:8-jre-alpine

COPY ./build/libs/eris-event-api.jar /root/eris-event-api.jar

WORKDIR /root

CMD ["java", "-server", "-Xms256m", "-Xmx1g", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-Dconfig.file=config/eris-event-api.conf", "-Dlogback.configurationFile=config/logback.xml", "-jar", "eris-event-api.jar"]

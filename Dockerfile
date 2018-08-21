FROM openjdk:8-jre-alpine

COPY ./build/libs/eris-event-api-1.0.0-capsule.jar /root/eris-event-api.jar
COPY ./config /root/config

WORKDIR /root

CMD ["java", "-server", "-Xms1g", "-Xmx1g", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-Dconfig.file=config/eris-event-api.conf", "-Dlogback.configurationFile=config/logback.xml", "-jar", "eris-event-api.jar"]

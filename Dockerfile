FROM openjdk:12.0.2-jdk
MAINTAINER SkyTeam <skyteam@gmail.com>

ENTRYPOINT ["java", "-Xmx200m", "-jar", "/usr/share/skygram/skygram-api.jar"]

ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/skygram/skygram-api.jar

EXPOSE 8080

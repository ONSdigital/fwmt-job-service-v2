FROM openjdk:8
MAINTAINER Kieran Wardle <kieran.wardle@ons.gov.uk>
ARG jar
VOLUME /tmp
COPY $jar jobsvc.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java -jar /jobsvc.jar" ]

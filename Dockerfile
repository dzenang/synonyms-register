FROM openjdk:17-alpine
MAINTAINER dzenang.com
COPY target/synonyms-register-1.0.0-SNAPSHOT.jar synonyms-register-1.0.0.jar
ENTRYPOINT ["java","-jar","/synonyms-register-1.0.0.jar"]
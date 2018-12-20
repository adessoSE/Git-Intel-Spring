FROM openjdk:8-alpine

# Required for starting application up.
RUN apk update && apk add bash

RUN mkdir -p /opt/app
ENV PROJECT_HOME /opt/app

COPY build/libs/gitstalker-datapull-0.1.0.jar $PROJECT_HOME/gitstalker-datapull-0.1.0.jar

WORKDIR $PROJECT_HOME

CMD ["java", "-Dspring.data.mongodb.uri=mongodb://git-intel-mongo:27017/springmongo-demo","-Djava.security.egd=file:/dev/./urandom","-jar","./gitstalker-datapull-0.1.0.jar"]
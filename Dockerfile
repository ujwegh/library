FROM maven:3.6.0-jdk-8

ENV PROJECT_DIR=/opt/project

RUN mkdir -p $PROJECT_DIR
WORKDIR $ROJECT_DIR

ADD./pom.xml $PROJECT_DIR

RUN mvn dependency:resolve

ADD ./rc/ $PROJECT_DIR/src
RUN mvn install

FROM openjdk:8-jdk

ENV PROJECT_DIR=/opt/project
RUN mkdir -p $PROJECT_DIR
WORKDIR $ROJECT_DIR
COPY --from=0 $ROJECT_DIR/target/library* $PROJECT_DIR/

EXPOSE 8080

CMD ["java", "-jar", "/opt/project/library-0.0.1-SNAPSHOT.jar"]




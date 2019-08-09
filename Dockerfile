FROM ubuntu:latest

RUN echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 boolean true" | debconf-set-selections
RUN apt-get update && apt-get install -y oracle-java8-installer maven

ADD ./target/library.jar library.jar

EXPOSE 8080

CMD ["java", "-jar", "library.jar"]




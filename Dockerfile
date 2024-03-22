FROM gradle:8.5-jdk17
LABEL authors="nazim"
WORKDIR /opt/app
COPY ./build/libs/Individual-BackEnd-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar Individual-BackEnd-0.0.1-SNAPSHOT.jar"]
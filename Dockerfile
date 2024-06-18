FROM gradle:8.5-jdk17
WORKDIR /opt/app
COPY ./build/libs/Individual-BackEnd-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar Individual-BackEnd-0.0.1-SNAPSHOT.jar"]
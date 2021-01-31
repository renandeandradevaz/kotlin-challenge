FROM gradle AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean test shadowJar --no-daemon

FROM openjdk:8-jre-slim
RUN mkdir /app
COPY csvs /tmp/kotlinchallenge/csvs
COPY --from=build /home/gradle/src/build/libs/*.jar /app/kotlinchallenge-all.jar
ENTRYPOINT java -jar /app/kotlinchallenge-all.jar ${FILE_NAME} ${DATE} ${PRECISION_DIGITS}
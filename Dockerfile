FROM maven:3.8-jdk-11 as builder
COPY . /usr/src/easybuggy/
WORKDIR /usr/src/easybuggy/
RUN mvn -B package

FROM openjdk:11-slim
COPY --from=builder /usr/src/easybuggy/target/easybuggy.jar /
CMD ["java", "-jar", "easybuggy.jar"]

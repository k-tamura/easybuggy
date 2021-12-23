FROM maven:3.8-jdk-8 as builder
COPY . /usr/src/easybuggy/
WORKDIR /usr/src/easybuggy/
RUN mvn -B package

FROM openjdk:8-slim
COPY --from=builder /usr/src/easybuggy/target/easybuggy.jar /
CMD ["java", "-jar", "easybuggy.jar"]

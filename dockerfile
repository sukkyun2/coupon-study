FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY . .

RUN chmod 777 ./mvnw
RUN ./mvnw clean package -DskipTests
RUN java -Djarmode=layertools -jar target/coupon-0.0.1.jar extract --destination target/extracted
RUN mkdir -p target/extracted && (cd target/extracted; jar -xf ../coupon-0.0.1.jar)

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG EXTRACTED=/workspace/app/target/extracted
COPY --from=build ${EXTRACTED}/dependencies/ ./
COPY --from=build ${EXTRACTED}/spring-boot-loader/ ./
COPY --from=build ${EXTRACTED}/snapshot-dependencies/ ./
COPY --from=build ${EXTRACTED}/application/ ./
ENTRYPOINT ["java","org.springframework.boot.loader.JarLauncher"]

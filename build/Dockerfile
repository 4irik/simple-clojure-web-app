FROM clojure:temurin-23-lein-alpine AS builder

RUN mkdir /app
WORKDIR /app

COPY . .

RUN cp -r .m2 /root

RUN lein deps
#RUN lein check
#RUN lein test
RUN lein uberjar

FROM bellsoft/liberica-runtime-container:jdk-21.0.5-crac-cds-slim-glibc

ARG version
ENV version=${version:-v0.0}

WORKDIR /app
COPY --from=builder /app/target/patient-0.1.0-SNAPSHOT-standalone.jar ./app.jar

CMD ["java", "-jar", "app.jar"]

FROM gradle:8.14.2-jdk21-alpine AS build

WORKDIR /code

COPY ./ ./

RUN gradle installBootDist --info --stacktrace

FROM eclipse-temurin:21-jre-alpine

WORKDIR /service

COPY --from=build /code/application/build/install/backend-boot /service

COPY docker/docker-entrypoint.sh /entrypoint
RUN chmod +x /entrypoint

ENTRYPOINT ["/entrypoint"]

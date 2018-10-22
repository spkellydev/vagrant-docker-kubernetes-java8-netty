FROM java:8
EXPOSE 8080
ADD /out/artifacts/media_monmouth_jar/media.monmouth.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]


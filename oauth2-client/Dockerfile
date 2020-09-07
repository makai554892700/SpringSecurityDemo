FROM java:8
ADD app.jar /app/
ADD client.yml /config/
EXPOSE 8101
ENTRYPOINT ["java","-Dfile.encoding=utf-8","-Dserver.port=8101","-jar","/app/app.jar"]



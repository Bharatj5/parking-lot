FROM openjdk:11
ADD /target/parking-app-*.jar parking-app.jar
ENTRYPOINT ["java","-jar","parking-app.jar"]
FROM openjdk:18
FROM mysql
ADD target/telegrambot.jar telegrambot.jar
ENTRYPOINT ["java","-jar","telegrambot.jar"]
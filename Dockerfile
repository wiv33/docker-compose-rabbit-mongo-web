FROM hirokimatsumoto/alpine-openjdk-11

MAINTAINER PS

ADD build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "--spring.profiles.active=prod"]
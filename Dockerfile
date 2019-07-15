FROM openjdk:8-jre-alpine
ADD build/libs/bank-account-manager-0.0.1.jar bank-account-manager-0.0.1.jar
ENTRYPOINT ["java", "-jar", "bank-account-manager-0.0.1.jar"]
ARG APP_INSIGHTS_AGENT_VERSION=2.5.1

# Application image

#FROM hmctspublic.azurecr.io/base/java:openjdk-11-distroless-1.2
FROM hmctspublic.azurecr.io/base/java:17-distroless

COPY lib/AI-Agent.xml /opt/app/
COPY build/libs/adoption-cos-api.jar /opt/app/

EXPOSE 4550
CMD [ "adoption-cos-api.jar" ]

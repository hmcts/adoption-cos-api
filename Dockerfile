ARG APP_INSIGHTS_AGENT_VERSION=3.2.6

# Application image

#FROM hmctspublic.azurecr.io/base/java:openjdk-11-distroless-1.2
FROM hmctspublic.azurecr.io/base/java:17-distroless

COPY lib/AI-Agent.xml /opt/app/
COPY lib/applicationinsights.json /opt/app/
COPY build/libs/adoption-cos-api.jar /opt/app/

EXPOSE 4550
CMD [ "adoption-cos-api.jar" ]

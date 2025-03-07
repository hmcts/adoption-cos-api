# Adoption Case Orchestration Service API [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

This API handles callbacks from CCD for the ADOPTION case type.

## Overview

                        ┌─────────────────┐
                        │                 │
                        │ADOPTION-COS-API │
                        │                 │
                        └───────▲─────────┘
                                │
                                │
                        ┌───────▼────────┐
                        │                │
                  ┌─────►      CCD       ◄─────┐
                  │     │                │     │
                  │     └────────────────┘     │
                  │                            │
          ┌───────┴─────────┐        ┌─────────┴───────┐
          │                 │        │                 │
          │ ADOPTION-WEB    │        │       XUI       │
          │                 │        │                 │
          └─────────────────┘        └─────────────────┘

## Building and deploying the application

### Building the application

The project uses [Gradle](https://gradle.org) as a build tool. It already contains
`./gradlew` wrapper script, so there's no need to install gradle.

To build the project execute the following command:

    ./gradlew build

### Running the application

Create the image of the application by executing the following command:

    ./gradlew assemble

Create docker image:

    docker-compose build

Run the distribution (created in `build/install/adoption-cos-api` directory)
by executing the following command

    docker-compose up

This will start the API container exposing the application's port
(set to `4550` in this template app).

The application exposes health endpoint (http://localhost:4550/health):

    curl http://localhost:4550/health

You should get a response similar to this:

    {"status":"UP","diskSpace":{"status":"UP","total":249644974080,"free":137188298752,"threshold":10485760}}


### Running the application locally with CCD and XUI

Preliminary steps:

* To build:

    `./gradlew build`

* Connect to F5 VPN:<br>
  Go to the webpage https://portal.platform.hmcts.net/ and follow the instruction to connect to F5 VPN.

* Then login to the subscription (you will need to get the subscription id from another dev):

    `az acr login --name hmctspublic --subscription [SUBSCRIPTION ID]`

You can now run the full CCD and XUI stack locally using:

    ./gradlew bootWithCcd

* Then you can access XUI on `http://localhost:3000`


### Generate CCD Excel Spreadsheet

To generate the CCD Excel files run the following from the root of the project:

    ./gradlew buildCCDXlsx

### Generate CCD JSON files

To generate the CCD JSON files from the Java Model run the following from the root of the project:

    ./gradlew generateCCDConfig

### Generate TypeScript definitions for CCD definition

    ./gradlew generateTypeScript


## Plugins

The project contains the following plugins:

* checkstyle

  https://docs.gradle.org/current/userguide/checkstyle_plugin.html

  Performs code style checks on Java source files using Checkstyle and generates reports from these checks.
  The checks are included in gradle's *check* task (you can run them by executing `./gradlew check` command).

* pmd

  https://docs.gradle.org/current/userguide/pmd_plugin.html

  Performs static code analysis to finds common programming flaws. Included in gradle `check` task.


* jacoco

  https://docs.gradle.org/current/userguide/jacoco_plugin.html

  Provides code coverage metrics for Java code via integration with JaCoCo.
  You can create the report by running the following command:

  ```bash
    ./gradlew jacocoTestReport
  ```

  The report will be created in build/reports subdirectory in your project directory.

* io.spring.dependency-management

  https://github.com/spring-gradle-plugins/dependency-management-plugin

  Provides Maven-like dependency management. Allows you to declare dependency management
  using `dependency 'groupId:artifactId:version'`
  or `dependency group:'group', name:'name', version:version'`.

* org.springframework.boot

  http://projects.spring.io/spring-boot/

  Reduces the amount of work needed to create a Spring application

* org.owasp.dependencycheck

  https://jeremylong.github.io/DependencyCheck/dependency-check-gradle/index.html

  Provides monitoring of the project's dependent libraries and creating a report
  of known vulnerable components that are included in the build. To run it
  execute `gradle dependencyCheck` command.

* com.github.ben-manes.versions

  https://github.com/ben-manes/gradle-versions-plugin

  Provides a task to determine which dependencies have updates. Usage:

  ```bash
    ./gradlew dependencyUpdates -Drevision=release
  ```

## Connect to Preview Database
Hostname = adoption-preview.postgres.database.azure.com
Port = 5432
Database Name = pr-<number>-data-store
Username = hmcts
SSL (Parameters) =  require

Password can be found in Kubernetes Services -> preview environment -> workloads
-> select deployment name adoption-cos-api-pr-<number>-ccd-data-store-api
-> select pod -> select container (overview) -> environment variables tab
-> click postgres link for DATA_STORE_DB_PASSWORD -> click eye to decrypt

To view the databases:
`kubectl get flexibleserversdatabases -n adoption`

## License



This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details


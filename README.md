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
by executing the following command:

    docker-compose up

This will start the API container exposing the application's port
(set to `4013` in this template app).

The application exposes health endpoint (http://localhost:4550/health) and metrics endpoint
(http://localhost:4550/metrics):

    curl http://localhost:4550/health

You should get a response similar to this:

    {"status":"UP","diskSpace":{"status":"UP","total":249644974080,"free":137188298752,"threshold":10485760}}

### Generate CCD JSON files

To generate the CCD JSON files from the Java Model run the following from the root of the project:

    ./gradlew ccd-definitions:generateCCDConfig

### Generate TypeScript definitions for CCD definition

    ./gradlew generateTypeScript

### Crons

You can manually run a cron task from the cli:

```
TASK_NAME=[task] java -jar adoption-cos-api.jar run

# E.g.
TASK_NAME=SystemProgressHeldCasesTask java -jar adoption-cos-api.jar

# or
TASK_NAME=SystemProgressHeldCasesTask ./gradlew bootRun
```

To configure a new cron in AAT please checkout the [cnp-flux-config](https://github.com/hmcts/cnp-flux-config/) repository and run:

```
./bin/add-cron.sh SystemProgressHeldCasesTask ~/cnp-flux-config "0/10 * * * *"
```

Then create a PR in the cnp-flux-config repository.

Note that the cron will only run in the aat-00 cluster as we don't have a way to run the job once over multiple clusters. Let's hope that cluster doesn't go down.

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

## Setup

Located in `./bin/init.sh`. Simply run and follow the explanation how to execute it.

## Notes

Since Spring Boot 2.1 bean overriding is disabled. If you want to enable it you will need to set `spring.main.allow-bean-definition-overriding` to `true`.

JUnit 5 is now enabled by default in the project. Please refrain from using JUnit4 and use the next generation

## Building and deploying the application

### Building the application

The project uses [Gradle](https://gradle.org) as a build tool. It already contains
`./gradlew` wrapper script, so there's no need to install gradle.

To build the project execute the following command:

```bash
  ./gradlew build
```

### Running the application

Create the image of the application by executing the following command:

```bash
  ./gradlew assemble
```

Create docker image:

```bash
  docker-compose build
```

Run the distribution (created in `build/install/adoption-cos-api` directory)
by executing the following command:

```bash
  docker-compose up
```

This will start the API container exposing the application's port
(set to `4550` in this app).

In order to test if the application is up, you can call its health endpoint:

```bash
  curl http://localhost:4550/health
```

You should get a response similar to this:

```json
{
  "status": "UP",
  "components": {
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 67371577344,
        "free": 59963727872,
        "threshold": 10485760,
        "exists": true
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

### Alternative script to run application

To skip all the setting up and building, just execute the following command:

```bash
./bin/run-in-docker.sh
```

For more information:

```bash
./bin/run-in-docker.sh -h
```

Script includes bare minimum environment variables necessary to start api instance. Whenever any variable is changed or any other script regarding docker image/container build, the suggested way to ensure all is cleaned up properly is by this command:

```bash
docker-compose rm
```

It clears stopped containers correctly. Might consider removing clutter of images too, especially the ones fiddled with:

```bash
docker images

docker image rm <image-id>
```

There is no need to remove postgres and java or similar core images.

### Other

Hystrix offers much more than Circuit Breaker pattern implementation or command monitoring.
Here are some other functionalities it provides:
* [Separate, per-dependency thread pools](https://github.com/Netflix/Hystrix/wiki/How-it-Works#isolation)
* [Semaphores](https://github.com/Netflix/Hystrix/wiki/How-it-Works#semaphores), which you can use to limit
  the number of concurrent calls to any given dependency
* [Request caching](https://github.com/Netflix/Hystrix/wiki/How-it-Works#request-caching), allowing
  different code paths to execute Hystrix Commands without worrying about duplicating work

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

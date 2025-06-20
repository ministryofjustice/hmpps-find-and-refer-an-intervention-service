# Find and Refer an Intervention Service

[![repo standards badge](https://img.shields.io/badge/endpoint.svg?&style=flat&logo=github&url=https%3A%2F%2Foperations-engineering-reports.cloud-platform.service.justice.gov.uk%2Fapi%2Fv1%2Fcompliant_public_repositories%2Fhmpps-template-kotlin)](https://operations-engineering-reports.cloud-platform.service.justice.gov.uk/public-report/hmpps-template-kotlin "Link to report")
[![CircleCI](https://circleci.com/gh/ministryofjustice/hmpps-template-kotlin/tree/main.svg?style=svg)](https://circleci.com/gh/ministryofjustice/hmpps-template-kotlin)
[![Docker Repository on Quay](https://img.shields.io/badge/quay.io-repository-2496ED.svg?logo=docker)](https://quay.io/repository/hmpps/hmpps-template-kotlin)
[![API docs](https://img.shields.io/badge/API_docs_-view-85EA2D.svg?logo=swagger)](https://hmpps-template-kotlin-dev.hmpps.service.justice.gov.uk/webjars/swagger-ui/index.html?configUrl=/v3/api-docs)

This repository contains the service code for the `Find and Refer an Intervention service`.

## Required software

Most software can be installed using [homebrew](https://brew.sh/).

* Docker
* Java SDK (OpenJDK 21)

## Running the application locally

The application comes with a `local` spring profile that includes default settings for running locally. This is not
necessary when deploying to kubernetes as these values are included in the helm configuration templates -
e.g. `values-dev.yaml`. This run configuration is included in the
project [local run configuration](./.run/Local.run.xml)
and can be accessed using the run options in the top right of the window in IntelliJ.

There is also a `docker-compose.yml` that can be used to run a local instance of the template in docker and also an
instance of HMPPS Auth.

Run the following command to pull the relevant dependencies for the project.

```bash
  docker-compose pull
```

and then the following command to run the containers.

```bash
  docker-compose up
```

can optionally be run in detached mode in order to retain terminal use

```bash
  docker-compose up -d
```

### Connecting to local database

The service uses a postgres database alongside flyaway migrations to create and populate the database. To connect to the
database locally in your preferred database
client ([IntelliJ Ultimate](https://www.jetbrains.com/help/idea/database-tool-window.html), [Dbeaver](https://dbeaver.io/),
[Pgadmin](https://www.pgadmin.org/), etc).

Create new connection using local database credentials;

| Variable | Value |
|----------|-------|
| Port     | 5432  |
| Username | root  |
| Password | dev   |

### Events

The application listens to events that are published on the `HMPPS_DOMAIN_EVENTS_QUEUE`. This functionality is
replicated locally using localstack. If you wish to view and create messages/queues/etc then it is recommended to use
the `awslocal` wrapper which can be installed using

```zsh
brew install awscli-local
```

You can view the created queue by running:

```zsh
awslocal sqs list-queues
```

There are example messages in the following directory [here](./src/test/resources/testData/events). To send one of these
events to our queues we can run the following command:

```zsh
 awslocal sqs send-message --queue-url http://sqs.eu-west-2.localhost.localstack.cloud:4566/000000000000/hmppsdomainevent  --message-body file://src/test/resources/testData/events/probationCaseRequirementCreatedEvent.json
```

### Authorization

The service uses an Oauth 2.0 setup managed through the Hmpps Auth project. To call any endpoints locally a bearer token
must be generated. This can be done through calling the auth endpoint in the Hmpps-auth service.

| Variable         | Value                                          |
|------------------|------------------------------------------------|
| Grant type       | Client credentials                             |
| Access token URL | http://hmpps-auth:9090/auth/oauth/token        |
| Client ID        | hmpps-find-and-refer-an-intervention-ui-client |
| Client Secret    | clientsecret                                   |
| Scope            | Read                                           |

The values for `ClientId` and `Client Secret` are the local values. These are the same credentials that the UI uses to
call this service.

## Branch naming validator

This project has a branch naming validator in place in the GitHub action pipeline.

To ensure these pipelines pass the branch name must conform one of the following patterns:

* FRI-[0-9]/*branch-name-here*
* no-ticket/*branch-name-here*
* renovate/*branch-name-here*
* hotfix/*branch-name-here*

## Formatting

This project is formatted using ktlint.

Run the following command to add a pre-commit hook to ensure your code is formatted correctly before pushing.

```bash
./gradlew addKtlintFormatGitPreCommitHook
```

## Interventions Data

For loading the Intervention Catalogue data both locally and in a higher environment, a Spring Batch job is utilised.
This reads all the .json files in a directory [here](src/main/resources/db/interventions) and maps these to the
corresponding db tables.

The job can be run manually using the following command to load the data locally

```zsh
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun --args=‘--jobName=upsertInterventionsJob’
```

For loading intervention metadata like delivery location, intervention catalogue map and intervention catalogue course
map both locally and in a higher environment, a Spring Batch job is utilised. This reads the respective .csv files in
the directory [here](src/main/resources/csv) and maps these to the corresponding db tables.

The job can be run manually using the following command to load the data locally

For loading Delivery location data, update the `delivery_location.csv` file in the
directory [here](src/main/resources/csv) and run the following command:

```zsh
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun --args=‘--jobName=loadDeliveryLocationJob’
```

For loading Intervention catalogue map data, update the `intervention_catalogue_map.csv` file in the
directory [here](src/main/resources/csv) and run the following command:

```zsh
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun --args=‘--jobName=loadInterventionCatalogueMapJob’
```

For loading Intervention catalogue course map data, update the `intervention_catalogue_course_map.csv` file in the
directory [here](src/main/resources/csv) and run the following command:

```zsh
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun --args=‘--jobName=loadInterventionCatalogueToCourseMapJob’
```

For future updates, we can just add the new rows to the end of the csv and the job will take care of inserting the new
row. We also have the feature of deleting any data already present via the csv. We have to mark the status column of the
particular row with 'D' and the job will delete that row from the database.

## Deployments

Deployments are part of our CI process, on the `main` branch using the `build-test-and-deploy` Workflow.

[This is a link](https://app.circleci.com/pipelines/github/ministryofjustice/hmpps-find-and-refer-an-intervention-service?branch=main)
to the most recent runs of that Workflow.

Deployments require a manual approval step.

### Testing a Deployment

The Find & Refer an Intervention Service is not presently live. We therefore do not have a Production environment
available.

It is only possible to do User Acceptance Testing (UAT), i.e. click around a browser, on our Dev environment.

To test a deployment to production, we have to examine the logs of a pod, to assert if it has spun up successfully or
not. This is obviously not ideal.

### Kubernetes

All deployments and environments are managed through Kubernetes.

For information on how to connect to the Cloud Platform's Kubernetes cluster follow the setup
guide [here](https://user-guide.cloud-platform.service.justice.gov.uk/documentation/getting-started/kubectl-config.html#connecting-to-the-cloud-platform-39-s-kubernetes-cluster).

For further Kubernetes commands a useful cheat sheet is
provided [here](https://kubernetes.io/docs/reference/kubectl/quick-reference/). Similarly, the `--help` flag on any
`kubectl` command will give you more information.

### Testing a Deployment

#### tl;dr

```zsh
kubectl get deployments -n hmpps-find-and-refer-an-intervention-prod
```

```zsh
kubectl get pods -n hmpps-find-and-refer-an-intervention-prod
```

```zsh
kubectl logs $POD_NAME --namespace hmpps-find-and-refer-an-intervention-prod
```

```zsh
kubectl scale deployment hmpps-find-and-refer-an-intervention-service --namespace hmpps-find-and-refer-an-intervention-prod --replicas=0
```

#### 1. Find the deployments in the `hmpps-find-and-refer-an-intervention-prod` namespace:

```zsh
$ kubectl get deployments -n hmpps-find-and-refer-an-intervention-prod

NAME                                           READY   UP-TO-DATE   AVAILABLE   AGE
hmpps-find-and-refer-an-intervention-service   0/0     0            0           41d
hmpps-find-and-refer-an-intervention-ui        2/2     2            2           41d
```

If you have done a deployment of UI, there should be more than 0 Pods marked as `READY` in that response, indicating
that they have, indeed, been spun up.

#### 2. Double-check the Pod(s) associated with the Deployment:

Per [Kubernete's docs](https://kubernetes.io/docs/concepts/workloads/pods/):

> A Pod is similar to a set of containers with shared namespaces and shared filesystem volumes.

View the Pods in the namespace, these are what the `READY` column in the `get deployments` refer to:

```zsh
$ kubectl get pods -n hmpps-find-and-refer-an-intervention-prod


NAME                                                            READY   STATUS    RESTARTS   AGE
hmpps-find-and-refer-an-intervention-service-58bb6f56b4-7q566   1/1     Running   0          35m
hmpps-find-and-refer-an-intervention-service-58bb6f56b4-kzqsn   1/1     Running   0          35m
```

#### 3. Check the logs of a Pod

It is possible to read the logs of a given Pod to check that the build and spin-up process for the Pod has been
successful.

To view the logs from any of the Pods whose name is given in the previous responses:

```zsh
$ kubectl logs $POD_NAME --namespace hmpps-find-and-refer-an-intervention-prod

# ...
Application Insights 2.X SDK. []
14:25:13.500Z  INFO HMPPS Find And Refer An Intervention Ui: Server listening on port 3000
```

Where `$POD_NAME` is the full string Pod name given in the `get pods` response.

#### 4. Scale down the Pods

While we are in pre-release, it's important not to leave the pods running.

We scale down the number of running Pods in the Kubernetes deployment with the following:

```zsh
$ kubectl scale deployment hmpps-find-and-refer-an-intervention-service --namespace hmpps-find-and-refer-an-intervention-prod --replicas=0

deployment.apps/hmpps-find-and-refer-an-intervention-ui scaled
```

And then double-check this has taken effect:

```zsh
$ kubectl get deployments --namespace=hmpps-find-and-refer-an-intervention-prod

NAME                                           READY   UP-TO-DATE   AVAILABLE   AGE
hmpps-find-and-refer-an-intervention-service   0/0     0            0           41d
hmpps-find-and-refer-an-intervention-ui        0/0     0            0           41d
```

By checking for the `0` in the `READY` column.

## Troubleshooting

For any issues please reach out to the Find and Refer Interventions team in
slack [#find-refer-interventions-team](https://moj.enterprise.slack.com/archives/C06MPNK0AD6)
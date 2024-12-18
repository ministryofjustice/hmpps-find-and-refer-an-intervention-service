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

| Variable | Value    |
|----------|----------|
| Port     | 5432     |
| Username | postgres |
| Password | password |

### Authorization

The service uses an Oauth 2.0 setup managed through the Hmpps Auth project. To call any endpoints locally a bearer token
must be generated. This can be done through calling the auth endpoint in the Hmpps-auth service.

| Variable         | Value                                   |
|------------------|-----------------------------------------|
| Grant type       | Client credentials                      |
| Access token URL | http://hmpps-auth:8090/auth/oauth/token |
| Client ID        | -----                                   |
| Client Secret    | -----                                   |
| Scope            | Read                                    |

For Client ID and Secret refer to the relevant credentials for the Find and Refer Project.

## Troubleshooting

For any issues please reach out to the Find and Refer Interventions team in
slack [#find-refer-interventions-team](https://moj.enterprise.slack.com/archives/C06MPNK0AD6)
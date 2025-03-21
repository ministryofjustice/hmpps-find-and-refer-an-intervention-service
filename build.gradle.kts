plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "7.1.4"
  id("org.jetbrains.kotlin.plugin.jpa") version "2.1.20"
  kotlin("plugin.spring") version "2.1.20"
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

dependencies {
  implementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter:1.4.2")
  implementation("org.springframework.boot:spring-boot-starter-webflux")

  // batch processing
  implementation("org.springframework.boot:spring-boot-starter-batch")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.3") // also needed runtime for AppInsights

  // monitoring and logging
  implementation("io.micrometer:micrometer-registry-prometheus")
  implementation("io.sentry:sentry-spring-boot-starter:8.5.0")
  implementation("io.sentry:sentry-logback:8.5.0")
  implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
  implementation("net.logstash.logback:logstash-logback-encoder:8.0")

  runtimeOnly("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.3") // needed for OffsetDateTime for AppInsights

  // openapi
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")
  implementation("javax.xml.bind:jaxb-api:2.3.1")
  // notifications
  implementation("uk.gov.service.notify:notifications-java-client:5.2.1-RELEASE")
  implementation("org.json:json") {
    version {
      strictly("20231013")
    }
  }

  // aws
  //  implementation("software.amazon.awssdk:sns:2.26.31")
  //  implementation("software.amazon.awssdk:s3:2.26.31")
  //  implementation("software.amazon.awssdk:sts:2.26.31")

  // security
  // implementation("org.springframework.boot:spring-boot-starter-webflux:3.3.2")
  // implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:3.3.2")
  // implementation("org.springframework.boot:spring-boot-starter-oauth2-client:3.3.2")
  // implementation("com.nimbusds:oauth2-oidc-sdk:11.14")

  // database
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("com.h2database:h2:2.3.232")
  implementation("org.hibernate:hibernate-core:6.6.11.Final")
  implementation("io.hypersistence:hypersistence-utils-hibernate-63:3.9.5")

  runtimeOnly("org.postgresql:postgresql:42.7.5")
  runtimeOnly("org.flywaydb:flyway-database-postgresql")

  // json and csv
  implementation("com.github.java-json-tools:json-patch:1.13")
  implementation("org.apache.commons:commons-csv:1.14.0")

  testImplementation("au.com.dius.pact.provider:junit5spring:4.6.17")
  testImplementation("com.squareup.okhttp3:okhttp:4.12.0")
  testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
  testImplementation("org.mockito:mockito-inline:5.2.0")
  testImplementation("org.springframework.batch:spring-batch-test")
  // testImplementation("com.github.tomakehurst:wiremock-jre8:3.0.1")

  testImplementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter-test:1.4.2")
  testImplementation("org.wiremock:wiremock-standalone:3.12.1")
  testImplementation("io.swagger.parser.v3:swagger-parser:2.1.25") {
    exclude(group = "io.swagger.core.v3")
  }
}

kotlin {
  jvmToolchain(21)
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions.jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
  }
}

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "9.2.0"
  id("org.jetbrains.kotlin.plugin.jpa") version "2.2.21"
  kotlin("plugin.spring") version "2.2.21"
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

dependencies {
  implementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter:1.8.2")
  implementation("org.springframework.boot:spring-boot-starter-webflux")

  // batch processing
  implementation("org.springframework.boot:spring-boot-starter-batch")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.20.1") // also needed runtime for AppInsights

  // monitoring and logging
  implementation("io.micrometer:micrometer-registry-prometheus")
  implementation("io.sentry:sentry-spring-boot-starter-jakarta:8.28.0")
  implementation("io.sentry:sentry-logback:8.28.0")
  implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
  implementation("net.logstash.logback:logstash-logback-encoder:9.0")

  runtimeOnly("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.20.1") // needed for OffsetDateTime for AppInsights

  // openapi
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.14")
  implementation("javax.xml.bind:jaxb-api:2.3.1")
  // notifications
  implementation("uk.gov.service.notify:notifications-java-client:6.0.0-RELEASE")
  implementation("org.json:json") {
    version {
      strictly("20231013")
    }
  }

  // SQN/SNS
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:5.6.3")
  // security
  implementation("org.springframework.boot:spring-boot-starter-webflux:3.5.8")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:3.5.8")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-client:3.5.8")
  implementation("org.springframework.security:spring-security-crypto:7.0.0")
  implementation("com.nimbusds:oauth2-oidc-sdk:11.30.1")
  implementation("org.apache.httpcomponents.client5:httpclient5:5.5.1")

  // database
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("com.h2database:h2:2.4.240")
  implementation("org.hibernate:hibernate-core:7.1.11.Final")
  implementation("io.hypersistence:hypersistence-utils-hibernate-63:3.13.2")

  // monitoring
  implementation("io.micrometer:micrometer-registry-prometheus")

  runtimeOnly("org.postgresql:postgresql:42.7.8")
  runtimeOnly("org.flywaydb:flyway-database-postgresql")

  // json and csv
  implementation("com.github.java-json-tools:json-patch:1.13")
  implementation("org.apache.commons:commons-csv:1.14.1")

  testImplementation("au.com.dius.pact.provider:junit5spring:4.6.18")
  testImplementation("com.squareup.okhttp3:okhttp:5.3.2")
  testImplementation("com.squareup.okhttp3:mockwebserver:5.3.2")
  testImplementation("org.mockito:mockito-inline:5.2.0")
  testImplementation("org.springframework.batch:spring-batch-test")
  testImplementation("org.springframework.security:spring-security-test")

  // Test containers
  testImplementation("org.testcontainers:postgresql:1.21.3")
  testImplementation("org.testcontainers:localstack:1.21.3")

  testImplementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter-test:1.8.2")
  testImplementation("org.wiremock:wiremock-standalone:3.13.2")
  testImplementation("io.swagger.parser.v3:swagger-parser:2.1.36") {
    exclude(group = "io.swagger.core.v3")
  }
  testImplementation("org.awaitility:awaitility-kotlin:4.3.0")
}

kotlin {
  jvmToolchain(21)
}

tasks {
  withType<KotlinCompile> {
    compilerOptions.jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
  }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.compilerOptions {
  freeCompilerArgs.set(listOf("-Xannotation-default-target=param-property"))
}

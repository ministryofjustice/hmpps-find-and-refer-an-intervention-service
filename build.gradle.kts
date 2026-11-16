import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("plugin.spring") version "2.4.21"
  id("org.jetbrains.kotlin.plugin.jpa") version "2.4.21"
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "11.+"
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

dependencies {
  implementation(enforcedPlatform("io.netty:netty-bom:4.2.20.Final"))

  implementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter:3.1.0")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-webclient")

  // batch processing
  implementation("org.springframework.boot:spring-boot-starter-batch")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.23.0") // also needed runtime for AppInsights

  // monitoring and logging
  implementation("io.micrometer:micrometer-registry-prometheus")
  implementation("io.sentry:sentry-spring-boot-4-starter:8.64.0")
  implementation("io.sentry:sentry-logback:8.64.0")
  implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
  implementation("net.logstash.logback:logstash-logback-encoder:9.0")

  runtimeOnly("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.23.0") // needed for OffsetDateTime for AppInsights

  // openapi
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.2.0")
  // notifications
  implementation("uk.gov.service.notify:notifications-java-client:6.2.1-RELEASE")
  implementation("org.json:json") {
    version {
      strictly("20231013")
    }
  }

  // SQN/SNS
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:7.5.0-beta-2")
  // security
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
  implementation("org.springframework.security:spring-security-crypto")
  implementation("com.nimbusds:oauth2-oidc-sdk:11.39")
  implementation("org.apache.httpcomponents.client5:httpclient5:5.6.5")

  // database
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("com.h2database:h2:2.5.252")
  // monitoring
  implementation("io.micrometer:micrometer-registry-prometheus")

  runtimeOnly("org.postgresql:postgresql:42.7.14")
  runtimeOnly("org.flywaydb:flyway-database-postgresql")
  implementation("org.springframework.boot:spring-boot-starter-flyway")

  // json and csv
  implementation("com.github.java-json-tools:json-patch:1.13")
  implementation("org.apache.commons:commons-csv:1.14.1")

  testImplementation("au.com.dius.pact.provider:junit5spring:4.7.7")
  testImplementation("com.squareup.okhttp3:okhttp:5.5.1")
  testImplementation("com.squareup.okhttp3:mockwebserver:5.5.1")
  testImplementation("org.mockito:mockito-inline:5.2.0")
  testImplementation("org.springframework.batch:spring-batch-test")
  testImplementation("org.springframework.security:spring-security-test")
  testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
  testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
  testImplementation("org.springframework.boot:spring-boot-starter-webflux-test")

  // Test containers

  testImplementation("org.testcontainers:postgresql:1.21.4")
  testImplementation("org.testcontainers:localstack:1.21.4")

  testImplementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter-test:3.1.0")
  testImplementation("org.wiremock:wiremock-standalone:3.13.2")
  testImplementation("io.swagger.parser.v3:swagger-parser:2.1.48") {
    exclude(group = "io.swagger.core.v3")
  }
  testImplementation("org.awaitility:awaitility-kotlin:4.3.0")
}

kotlin {
  jvmToolchain(25)
}

tasks {
  withType<KotlinCompile> {
    compilerOptions.jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_25
  }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.compilerOptions {
  freeCompilerArgs.set(listOf("-Xannotation-default-target=param-property"))
}

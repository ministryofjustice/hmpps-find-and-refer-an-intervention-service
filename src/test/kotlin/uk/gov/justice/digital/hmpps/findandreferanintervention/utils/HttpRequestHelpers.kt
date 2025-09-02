package uk.gov.justice.digital.hmpps.findandreferanintervention.utils

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.HeaderAssertions
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriBuilder
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse
import java.net.URI

fun makeRequest(
  testClient: WebTestClient,
  httpMethod: HttpMethod,
  uri: (UriBuilder) -> URI,
  requestCustomizer: WebTestClient.RequestHeadersSpec<*>.() -> Unit,
): WebTestClient.ResponseSpec = testClient.method(httpMethod)
  .uri(uri)
  .apply(requestCustomizer)
  .exchange()

fun <T> makeRequestAndExpectJsonResponse(
  testClient: WebTestClient,
  httpMethod: HttpMethod,
  uri: (UriBuilder) -> URI,
  requestCustomizer: WebTestClient.RequestHeadersSpec<*>.() -> Unit,
  expectedStatus: HttpStatus,
  responseType: Class<T>,
  expectedResponse: T & Any,
): WebTestClient.BodySpec<*, *> = testClient.method(httpMethod)
  .uri(uri)
  .apply(requestCustomizer)
  .exchange()
  .expectStatus()
  .isEqualTo(expectedStatus)
  .expectHeader()
  .contentType(MediaType.APPLICATION_JSON)
  .expectBody(responseType)
  .isEqualTo(expectedResponse)

fun makeRequestAndExpectJsonPathResponse(
  testClient: WebTestClient,
  httpMethod: HttpMethod,
  uri: (UriBuilder) -> URI,
  requestCustomizer: WebTestClient.RequestHeadersSpec<*>.() -> Unit,
  fieldName: String,
  expectedValue: String,
): WebTestClient.BodyContentSpec = testClient.method(httpMethod)
  .uri(uri)
  .apply(requestCustomizer)
  .exchange()
  .expectStatus()
  .isOk
  .expectBody()
  .jsonPath(fieldName).isEqualTo(expectedValue)

fun makeRequestAndExpectStatus(
  testClient: WebTestClient,
  httpMethod: HttpMethod,
  uri: (UriBuilder) -> URI,
  requestCustomizer: WebTestClient.RequestHeadersSpec<*>.() -> Unit,
  expectedStatus: HttpStatus,
): HeaderAssertions = testClient.method(httpMethod)
  .uri(uri)
  .apply(requestCustomizer)
  .exchange()
  .expectStatus()
  .isEqualTo(expectedStatus)
  .expectHeader()

fun makeErrorResponse(
  status: HttpStatus,
  errorCode: String? = null,
  userMessage: String? = null,
  developerMessage: String? = null,
  moreInfo: String? = null,
) = ErrorResponse(
  status = status,
  errorCode = errorCode,
  userMessage = userMessage,
  developerMessage = developerMessage,
  moreInfo = moreInfo,
)

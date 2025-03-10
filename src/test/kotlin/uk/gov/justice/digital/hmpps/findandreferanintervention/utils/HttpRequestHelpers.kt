package uk.gov.justice.digital.hmpps.findandreferanintervention.utils

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriBuilder
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse
import java.net.URI

fun <T> makeRequestAndExpectJsonResponse(
  testClient: WebTestClient,
  httpMethod: HttpMethod,
  uri: (UriBuilder) -> URI,
  requestCustomizer: WebTestClient.RequestHeadersSpec<*>.() -> Unit,
  expectedStatus: HttpStatus,
  responseType: Class<T>,
  expectedResponse: T & Any,
) {
  testClient.method(httpMethod)
    .uri(uri)
    .apply(requestCustomizer)
    .exchange()
    .expectStatus()
    .isEqualTo(expectedStatus)
    .expectHeader()
    .contentType(MediaType.APPLICATION_JSON)
    .expectBody(responseType)
    .isEqualTo(expectedResponse)
}

fun makeRequestAndExpectStatus(
  testClient: WebTestClient,
  httpMethod: HttpMethod,
  uri: (UriBuilder) -> URI,
  requestCustomizer: WebTestClient.RequestHeadersSpec<*>.() -> Unit,
  expectedStatus: HttpStatus,
) {
  testClient.method(httpMethod)
    .uri(uri)
    .apply(requestCustomizer)
    .exchange()
    .expectStatus()
    .isEqualTo(expectedStatus)
    .expectHeader()
}

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

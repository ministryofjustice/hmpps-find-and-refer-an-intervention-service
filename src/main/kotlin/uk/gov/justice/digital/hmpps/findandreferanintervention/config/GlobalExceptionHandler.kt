package uk.gov.justice.digital.hmpps.findandreferanintervention.config

import io.sentry.Sentry
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.resource.NoResourceFoundException
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse

@RestControllerAdvice
class GlobalExceptionHandler {
  @ExceptionHandler(ValidationException::class)
  fun handleValidationException(e: ValidationException, request: HttpServletRequest): ResponseEntity<ErrorResponse> = ResponseEntity.status(BAD_REQUEST)
    .body(
      ErrorResponse(
        status = BAD_REQUEST,
        userMessage = "Validation failure: ${e.message}",
        developerMessage = e.message,
      ),
    )
    .also {
      captureToSentry(e, request)
      log.info("Validation exception: {}", e.message)
    }

  @ExceptionHandler(NoResourceFoundException::class)
  fun handleNoResourceFoundException(e: NoResourceFoundException, request: HttpServletRequest): ResponseEntity<ErrorResponse> = ResponseEntity.status(NOT_FOUND)
    .body(
      ErrorResponse(
        status = NOT_FOUND,
        userMessage = "No resource found failure: ${e.message}",
        developerMessage = e.message,
      ),
    )
    .also {
      captureToSentry(e, request)
      log.info("No resource found exception: {}", e.message)
    }

  @ExceptionHandler(AccessDeniedException::class)
  fun handleAccessDeniedException(e: AccessDeniedException, request: HttpServletRequest): ResponseEntity<ErrorResponse> = ResponseEntity.status(FORBIDDEN)
    .body(
      ErrorResponse(
        status = FORBIDDEN,
        userMessage = "Forbidden: ${e.message}",
        developerMessage = e.message,
      ),
    )
    .also {
      captureToSentry(e, request)
      log.error("Forbidden (403) returned: {} caller: {} path: {}", e.message, callingClientId(), request.requestURI)
    }

  @ExceptionHandler(Exception::class)
  fun handleException(e: Exception, request: HttpServletRequest): ResponseEntity<ErrorResponse> = ResponseEntity.status(INTERNAL_SERVER_ERROR)
    .body(
      ErrorResponse(
        status = INTERNAL_SERVER_ERROR,
        userMessage = "Unexpected error: ${e.message}",
        developerMessage = e.message,
      ),
    )
    .also {
      captureToSentry(e, request)
      log.error("Unexpected exception on {} {}", request.method, request.requestURI, e)
    }

  @ExceptionHandler(MethodArgumentTypeMismatchException::class)
  @ResponseStatus(BAD_REQUEST)
  fun handleEnumMismatchException(e: MethodArgumentTypeMismatchException, request: HttpServletRequest): ResponseEntity<ErrorResponse> = ResponseEntity.status(BAD_REQUEST)
    .body(
      ErrorResponse(
        status = BAD_REQUEST,
        userMessage = "Invalid value for parameter ${e.parameter.parameterName}",
        developerMessage = e.message,
      ),
    )
    .also {
      captureToSentry(e, request)
      log.error("Enum Mismatch exception: {}", e.message)
    }

  @ExceptionHandler(ResponseStatusException::class)
  fun handleResponseException(e: ResponseStatusException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
    val status = HttpStatus.valueOf(e.statusCode.value())
    return ResponseEntity.status(status)
      .body(
        ErrorResponse(status = status, userMessage = e.reason, developerMessage = e.message),
      ).also {
        captureToSentry(e, request)
        log.error("Response Status exception: {} caller: {} path: {}", e.message, callingClientId(), request.requestURI)
      }
  }

  @ExceptionHandler(HandlerMethodValidationException::class)
  fun handleConstraintViolationException(ex: HandlerMethodValidationException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
    val violationMessages = ex.allErrors.joinToString("; ") { it.defaultMessage ?: "Validation error" }

    return ResponseEntity.status(BAD_REQUEST)
      .body(
        ErrorResponse(
          status = BAD_REQUEST,
          userMessage = "Validation failure: $violationMessages",
          developerMessage = ex.message,
        ),
      )
      .also {
        captureToSentry(ex, request)
        log.info("Input request not matching the pattern: {}", violationMessages)
      }
  }

  @ExceptionHandler(WebClientResponseException.NotFound::class)
  fun handleNotFound(ex: WebClientResponseException.NotFound, request: HttpServletRequest): ResponseEntity<ErrorResponse> = ResponseEntity.status(NOT_FOUND)
    .body(
      ErrorResponse(status = NOT_FOUND, userMessage = ex.message, developerMessage = ex.message),
    ).also {
      captureToSentry(ex, request)
      log.error("External service data not found calling {} {}: {} body: {}", ex.request?.method, ex.request?.uri, ex.message, ex.responseBodyAsString)
    }

  @ExceptionHandler(WebClientResponseException.Unauthorized::class)
  fun handleUnauthorized(ex: WebClientResponseException.Unauthorized, request: HttpServletRequest): ResponseEntity<ErrorResponse> = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
    ErrorResponse(status = HttpStatus.UNAUTHORIZED, userMessage = ex.message, developerMessage = ex.message),
  ).also {
    captureToSentry(ex, request)
    log.error("External service unauthorized calling {} {}: {}", ex.request?.method, ex.request?.uri, ex.message)
  }

  @ExceptionHandler(WebClientResponseException.InternalServerError::class)
  fun handleServerError(ex: WebClientResponseException.InternalServerError, request: HttpServletRequest): ResponseEntity<ErrorResponse> = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
    ErrorResponse(status = HttpStatus.INTERNAL_SERVER_ERROR, userMessage = ex.message, developerMessage = ex.message),
  ).also {
    captureToSentry(ex, request)
    log.error("External service threw internal server error calling {} {}: {} body: {}", ex.request?.method, ex.request?.uri, ex.message, ex.responseBodyAsString)
  }

  @ExceptionHandler(WebClientResponseException::class)
  fun handleOtherWebClientErrors(ex: WebClientResponseException, request: HttpServletRequest): ResponseEntity<ErrorResponse> = ResponseEntity.status(ex.statusCode).body(
    ErrorResponse(status = HttpStatus.valueOf(ex.statusCode.value()), userMessage = ex.localizedMessage, developerMessage = ex.message),
  ).also {
    captureToSentry(ex, request)
    log.error("External service threw an error calling {} {}: {} body: {}", ex.request?.method, ex.request?.uri, ex.message, ex.responseBodyAsString)
  }

  @ExceptionHandler(Throwable::class)
  fun handleException(e: Throwable, request: HttpServletRequest): ResponseEntity<ErrorResponse?>? = ResponseEntity
    .status(INTERNAL_SERVER_ERROR)
    .body(
      ErrorResponse(
        status = INTERNAL_SERVER_ERROR,
        userMessage = "Unexpected error: ${e.message}",
        developerMessage = e.message,
      ).also {
        captureToSentry(e, request)
        log.error("Unexpected error on {} {}", request.method, request.requestURI, e)
      },
    )

  private fun captureToSentry(e: Throwable, request: HttpServletRequest) {
    Sentry.withScope { scope ->
      scope.setTag("http.method", request.method)
      scope.setTag("http.path", request.requestURI)
      callingClientId()?.let { scope.setTag("caller.client_id", it) }
      resourceIdFromPath(request.requestURI)?.let { scope.setTag("resource.id", it) }
      Sentry.captureException(e)
    }
  }

  private fun callingClientId(): String? = (SecurityContextHolder.getContext().authentication as? JwtAuthenticationToken)
    ?.token?.getClaimAsString("client_id")

  private fun resourceIdFromPath(path: String): String? = UUID_PATTERN.find(path)?.value

  private companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val UUID_PATTERN = Regex("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")
  }
}

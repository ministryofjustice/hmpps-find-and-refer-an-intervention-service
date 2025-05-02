package uk.gov.justice.digital.hmpps.findandreferanintervention.config

import jakarta.validation.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.resource.NoResourceFoundException
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse

@RestControllerAdvice
class GlobalExceptionHandler {
  @ExceptionHandler(ValidationException::class)
  fun handleValidationException(e: ValidationException): ResponseEntity<ErrorResponse> = ResponseEntity.status(BAD_REQUEST)
    .body(
      ErrorResponse(
        status = BAD_REQUEST,
        userMessage = "Validation failure: ${e.message}",
        developerMessage = e.message,
      ),
    )
    .also { log.info("Validation exception: {}", e.message) }

  @ExceptionHandler(NoResourceFoundException::class)
  fun handleNoResourceFoundException(e: NoResourceFoundException): ResponseEntity<ErrorResponse> = ResponseEntity.status(NOT_FOUND)
    .body(
      ErrorResponse(
        status = NOT_FOUND,
        userMessage = "No resource found failure: ${e.message}",
        developerMessage = e.message,
      ),
    )
    .also { log.info("No resource found exception: {}", e.message) }

  @ExceptionHandler(AccessDeniedException::class)
  fun handleAccessDeniedException(e: AccessDeniedException): ResponseEntity<ErrorResponse> = ResponseEntity.status(FORBIDDEN)
    .body(
      ErrorResponse(
        status = FORBIDDEN,
        userMessage = "Forbidden: ${e.message}",
        developerMessage = e.message,
      ),
    )
    .also { log.error("Forbidden (403) returned: {}", e.message) }

  @ExceptionHandler(Exception::class)
  fun handleException(e: Exception): ResponseEntity<ErrorResponse> = ResponseEntity.status(INTERNAL_SERVER_ERROR)
    .body(
      ErrorResponse(
        status = INTERNAL_SERVER_ERROR,
        userMessage = "Unexpected error: ${e.message}",
        developerMessage = e.message,
      ),
    )
    .also { log.error("Unexpected exception", e) }

  @ExceptionHandler(MethodArgumentTypeMismatchException::class)
  @ResponseStatus(BAD_REQUEST)
  fun handleEnumMismatchException(e: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponse> = ResponseEntity.status(BAD_REQUEST)
    .body(
      ErrorResponse(
        status = BAD_REQUEST,
        userMessage = "Invalid value for parameter ${e.parameter.parameterName}",
        developerMessage = e.message,
      ),
    )
    .also {
      log.error("Enum Mismatch exception: {}", e.message)
    }

  @ExceptionHandler(ResponseStatusException::class)
  @ResponseStatus(NOT_FOUND)
  fun handleResponseException(e: ResponseStatusException): ResponseEntity<ErrorResponse> = ResponseEntity.status(NOT_FOUND)
    .body(
      ErrorResponse(status = NOT_FOUND, userMessage = e.reason, developerMessage = e.message),
    ).also { log.error("Response Status exception: {}", e.message) }

  @ExceptionHandler(HandlerMethodValidationException::class)
  fun handleConstraintViolationException(ex: HandlerMethodValidationException): ResponseEntity<ErrorResponse> {
    val violationMessages = ex.allErrors.joinToString("; ") { it.defaultMessage ?: "Validation error" }

    return ResponseEntity.status(BAD_REQUEST)
      .body(
        ErrorResponse(
          status = BAD_REQUEST,
          userMessage = "Validation failure: $violationMessages",
          developerMessage = ex.message,
        ),
      )
      .also { log.info("Input request not matching the pattern: {}", violationMessages) }
  }

  private companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}

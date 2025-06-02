package uk.gov.justice.digital.hmpps.findandreferanintervention.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.findandreferanintervention.client.FindAndReferRestClient
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.ServiceUserDto
import java.time.LocalDate
import kotlin.text.get

class ServiceUserServiceTest {

  private val findPersonLocation = "/offenders/{identifier}"
  private val laocLocation = "/users/{username}/access/{identifier}"
  private val findAndReferDeliusApiClient: FindAndReferRestClient = mock(FindAndReferRestClient::class.java)
  private val serviceUserService = ServiceUserService(findPersonLocation, laocLocation, findAndReferDeliusApiClient)

  @Test
  fun `should return ServiceUserDto when valid identifier is provided`() {
    // Arrange
    val identifier = "X718255"
    val offenderResponse = OffenderIdentifiersResponse(
      crn = "X718255",
      nomsNumber = "N12345",
      name = OffenderName(forename = "Mitchell", surname = "Marsh"),
      dateOfBirth = "1990-01-01",
      ethnicity = "White",
      gender = "Male",
      probationDeliveryUnit = ProbationDeliveryUnit(code = "Leeds", description = "Unit 1"),
      setting = "Custody",
    )
    val expectedDto = ServiceUserDto(
      name = "Mitchell Marsh",
      crn = "X718255",
      dob = LocalDate.parse("1990-01-01"),
      gender = "Male",
      ethnicity = "White",
      currentPdu = "Leeds",
      setting = "Custody",
    )

    val offenderIdentifiersPath = UriComponentsBuilder.fromPath(findPersonLocation)
      .buildAndExpand(identifier)
      .toString()

    val requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec::class.java)
    val responseSpecMock = mock(WebClient.ResponseSpec::class.java)

    `when`(findAndReferDeliusApiClient.get(offenderIdentifiersPath))
      .thenReturn(requestHeadersSpecMock)
    `when`(requestHeadersSpecMock.retrieve())
      .thenReturn(responseSpecMock)
    `when`(responseSpecMock.bodyToMono(OffenderIdentifiersResponse::class.java))
      .thenReturn(Mono.just(offenderResponse))

    // Act
    val result = serviceUserService.getServiceUserByIdentifier(identifier)

    // Assert
    assertEquals(expectedDto, result)
    verify(findAndReferDeliusApiClient).get(offenderIdentifiersPath)
    verify(requestHeadersSpecMock).retrieve()
    verify(responseSpecMock).bodyToMono(OffenderIdentifiersResponse::class.java)
  }

  @Test
  fun `should return ServiceUserDto when some non mandatory elements are not present`() {
    // Arrange
    val identifier = "X718255"
    val offenderResponse = OffenderIdentifiersResponse(
      crn = "X718255",
      nomsNumber = null,
      name = OffenderName(forename = "Mitchell", surname = "Marsh"),
      dateOfBirth = "1990-01-01",
      ethnicity = null,
      gender = null,
      probationDeliveryUnit = ProbationDeliveryUnit(code = null, description = "Unit 1"),
      setting = "Custody",
    )
    val expectedDto = ServiceUserDto(
      name = "Mitchell Marsh",
      crn = "X718255",
      dob = LocalDate.parse("1990-01-01"),
      gender = null,
      ethnicity = null,
      currentPdu = null,
      setting = "Custody",
    )

    val offenderIdentifiersPath = UriComponentsBuilder.fromPath(findPersonLocation)
      .buildAndExpand(identifier)
      .toString()

    val requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec::class.java)
    val responseSpecMock = mock(WebClient.ResponseSpec::class.java)

    `when`(findAndReferDeliusApiClient.get(offenderIdentifiersPath))
      .thenReturn(requestHeadersSpecMock)
    `when`(requestHeadersSpecMock.retrieve())
      .thenReturn(responseSpecMock)
    `when`(responseSpecMock.bodyToMono(OffenderIdentifiersResponse::class.java))
      .thenReturn(Mono.just(offenderResponse))

    // Act
    val result = serviceUserService.getServiceUserByIdentifier(identifier)

    // Assert
    assertEquals(expectedDto, result)
    verify(findAndReferDeliusApiClient).get(offenderIdentifiersPath)
    verify(requestHeadersSpecMock).retrieve()
    verify(responseSpecMock).bodyToMono(OffenderIdentifiersResponse::class.java)
  }

  @Test
  fun `should return null when no offender is found`() {
    // Arrange
    val identifier = "A12345"
    val offenderIdentifiersPath = UriComponentsBuilder.fromPath(findPersonLocation)
      .buildAndExpand(identifier)
      .toString()

    val requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec::class.java)
    val responseSpecMock = mock(WebClient.ResponseSpec::class.java)

    `when`(findAndReferDeliusApiClient.get(offenderIdentifiersPath))
      .thenReturn(requestHeadersSpecMock)
    `when`(requestHeadersSpecMock.retrieve())
      .thenReturn(responseSpecMock)
    `when`(responseSpecMock.bodyToMono(OffenderIdentifiersResponse::class.java))
      .thenReturn(Mono.empty())

    // Act
    val result = serviceUserService.getServiceUserByIdentifier(identifier)

    // Assert
    assertEquals(null, result)
    verify(findAndReferDeliusApiClient).get(offenderIdentifiersPath)
    verify(requestHeadersSpecMock).retrieve()
    verify(responseSpecMock).bodyToMono(OffenderIdentifiersResponse::class.java)
  }

  @Test
  fun `should return true when user is not excluded and not restricted`() {
    val username = "Valerie Wyman"
    val identifier = "X718255"
    val laocPath = "/users/$username/access/$identifier"
    val response = LimitedAccessOffenderCheckResponse(
      crn = identifier,
      userExcluded = false,
      userRestricted = false,
    )

    val requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec::class.java)
    val responseSpecMock = mock(WebClient.ResponseSpec::class.java)

    `when`(findAndReferDeliusApiClient.get(laocPath)).thenReturn(requestHeadersSpecMock)
    `when`(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock)
    `when`(responseSpecMock.bodyToMono(LimitedAccessOffenderCheckResponse::class.java)).thenReturn(Mono.just(response))

    val result = serviceUserService.checkIfAuthenticatedDeliusUserHasAccessToServiceUser(username, identifier)

    assertTrue(result)
    verify(findAndReferDeliusApiClient).get(laocPath)
  }

  @Test
  fun `should return false when user is excluded`() {
    val username = "user2"
    val identifier = "X12345"
    val laocPath = "/users/$username/access/$identifier"
    val response = LimitedAccessOffenderCheckResponse(
      crn = identifier,
      userExcluded = true,
      userRestricted = false,
    )

    val requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec::class.java)
    val responseSpecMock = mock(WebClient.ResponseSpec::class.java)

    `when`(findAndReferDeliusApiClient.get(laocPath)).thenReturn(requestHeadersSpecMock)
    `when`(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock)
    `when`(responseSpecMock.bodyToMono(LimitedAccessOffenderCheckResponse::class.java)).thenReturn(Mono.just(response))

    val result = serviceUserService.checkIfAuthenticatedDeliusUserHasAccessToServiceUser(username, identifier)

    assertFalse(result)
  }

  @Test
  fun `should return false when user is restricted`() {
    val username = "user3"
    val identifier = "X12345"
    val laocPath = "/users/$username/access/$identifier"
    val response = LimitedAccessOffenderCheckResponse(
      crn = identifier,
      userExcluded = false,
      userRestricted = true,
    )

    val requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec::class.java)
    val responseSpecMock = mock(WebClient.ResponseSpec::class.java)

    `when`(findAndReferDeliusApiClient.get(laocPath)).thenReturn(requestHeadersSpecMock)
    `when`(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock)
    `when`(responseSpecMock.bodyToMono(LimitedAccessOffenderCheckResponse::class.java)).thenReturn(Mono.just(response))

    val result = serviceUserService.checkIfAuthenticatedDeliusUserHasAccessToServiceUser(username, identifier)

    assertFalse(result)
  }

  @Test
  fun `should return false when response is null`() {
    val username = "user4"
    val identifier = "X12345"
    val laocPath = "/users/$username/access/$identifier"

    val requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec::class.java)
    val responseSpecMock = mock(WebClient.ResponseSpec::class.java)

    `when`(findAndReferDeliusApiClient.get(laocPath)).thenReturn(requestHeadersSpecMock)
    `when`(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock)
    `when`(responseSpecMock.bodyToMono(LimitedAccessOffenderCheckResponse::class.java)).thenReturn(Mono.empty())

    val result = serviceUserService.checkIfAuthenticatedDeliusUserHasAccessToServiceUser(username, identifier)

    assertFalse(result)
  }
}

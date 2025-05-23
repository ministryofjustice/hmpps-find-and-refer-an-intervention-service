package uk.gov.justice.digital.hmpps.findandreferanintervention.service

import org.junit.jupiter.api.Assertions.assertEquals
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

class ServiceUserServiceTest {

  private val findPersonLocation = "/offenders/{identifier}"
  private val findAndReferDeliusApiClient: FindAndReferRestClient = mock(FindAndReferRestClient::class.java)
  private val serviceUserService = ServiceUserService(findPersonLocation, findAndReferDeliusApiClient)

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
}

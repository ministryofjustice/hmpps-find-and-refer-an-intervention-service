package uk.gov.justice.digital.hmpps.findandreferanintervention.controller

import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.Dummy
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.DummyRepository

@RestController
@PreAuthorize("hasRole('ROLE_INTERVENTIONS_API_READ_ALL')")
class DummyController(
  private val dummyRepository: DummyRepository,
) {
  @GetMapping("/dummy/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
  fun getDummy(@PathVariable id: Int): Dummy? {
    return dummyRepository.findByDummyId(id)
  }
}

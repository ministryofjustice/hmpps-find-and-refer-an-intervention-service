package uk.gov.justice.digital.hmpps.hmppsfindandreferaninterventionsservice.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@PreAuthorize("hasRole('ROLE_INTERVENTIONS_API_READ_ALL')")
class HelloController() {
  @GetMapping("/hello")
  fun getHello(): String {
    return "Hello from skeleton"
  }
}

package uk.gov.justice.digital.hmpps.findandreferanintervention

import mu.KLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication()
class FindAndReferAnIntervention(args: Array<String>) {
  companion object : KLogging()
}

fun main(args: Array<String>) {
  runApplication<FindAndReferAnIntervention>(*args)
}

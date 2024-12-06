package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.controller

import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.findandreferanintervention.controller.HelloController

internal class HelloControllerTest {
  private val helloController =
    HelloController()

  @Test
  fun `getHello returns message`() {
    val response = helloController.getHello()

    assertSame("Hello from skeleton", response)
  }
}

package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.jpa.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.DummyRepository

@DataJpaTest
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DummyRepositoryTest {

  @Autowired
  private lateinit var dummyRepository: DummyRepository

  @Test
  fun `whenFindById exists then return Dummy`() {
    val dummy = dummyRepository.findByDummyId(1)
    assertThat(dummy?.dummyDescription).isEqualTo("dummy text 1")
  }

  @Test
  fun `whenFindById doesn't exist then return nothing`() {
    val dummy = dummyRepository.findByDummyId(50)
    assertThat(dummy).isNull()
  }
}

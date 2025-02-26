package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.ServiceProvider
import java.util.*

interface ServiceProviderRepository : JpaRepository<ServiceProvider, UUID>

package uk.gov.justice.digital.hmpps.findandreferanintervention.service

import com.microsoft.applicationinsights.TelemetryClient
import org.springframework.stereotype.Service

// Will be replaced by the Agent during runtime
@Service
class DummyTelemetryClient : TelemetryClient()

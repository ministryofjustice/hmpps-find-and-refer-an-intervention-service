package uk.gov.justice.digital.hmpps.findandreferanintervention

import com.microsoft.applicationinsights.TelemetryClient

object Utils {
  private var telemetryClient = TelemetryClient()

  fun logToAppInsights(page: String, messages: Map<String, String>) = telemetryClient.trackEvent(page, messages, null)
}

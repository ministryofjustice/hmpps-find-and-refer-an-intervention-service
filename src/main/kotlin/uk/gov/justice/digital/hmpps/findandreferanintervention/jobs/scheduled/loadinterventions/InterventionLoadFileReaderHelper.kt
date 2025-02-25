package uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions

import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.util.FileCopyUtils
import java.nio.charset.StandardCharsets

class InterventionLoadFileReaderHelper {

  companion object {

    fun getResource(path: String): String {
      val loader = this::class.java.classLoader
      val resolver = PathMatchingResourcePatternResolver(loader)
      val resource = resolver.getResource(path)
      val binaryData = FileCopyUtils.copyToByteArray(resource.inputStream)

      return String(binaryData, StandardCharsets.UTF_8)
    }

    fun getResourceUrls(path: String): List<String> {
      val loader = this::class.java.classLoader
      val resolver = PathMatchingResourcePatternResolver(loader)
      val resources = resolver.getResources(path)

      return resources.map { it.url.toExternalForm() }
    }
  }
}

package uk.gov.justice.digital.hmpps.findandreferanintervention.loadinterventions

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class InterventionUniqueUuidTest {

  private val objectMapper = ObjectMapper()

  @Test
  fun `all intervention JSON files must have unique uuid`() {
    val resourceDir = Paths.get("src/main/resources/db/interventions")
    val uuidToFile = mutableMapOf<String, String>()

    Files.list(resourceDir).use { paths ->
      paths.filter { it.toString().endsWith(".json") }.forEach { path ->
        val json: JsonNode = objectMapper.readTree(Files.newBufferedReader(path))
        val uuid = json.get("uuid")?.asText()
        require(!uuid.isNullOrBlank()) { "Missing uuid in file: $path" }
        val previous = uuidToFile.put(uuid, path.fileName.toString())
        assertTrue(
          previous == null,
          "Duplicate uuid '$uuid' found in files: $previous and ${path.fileName}",
        )
      }
    }
  }
}

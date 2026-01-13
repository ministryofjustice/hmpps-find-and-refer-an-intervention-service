package uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled

import org.apache.commons.csv.CSVFormat
import org.springframework.batch.core.job.parameters.JobParameters
import org.springframework.batch.core.job.parameters.JobParametersBuilder
import org.springframework.batch.core.job.parameters.JobParametersIncrementer
import org.springframework.batch.infrastructure.item.file.FlatFileHeaderCallback
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemWriterBuilder
import org.springframework.batch.infrastructure.item.file.transform.BeanWrapperFieldExtractor
import org.springframework.batch.infrastructure.item.file.transform.ExtractorLineAggregator
import org.springframework.batch.infrastructure.item.file.transform.RecursiveCollectionLineAggregator
import org.springframework.core.io.WritableResource
import org.springframework.stereotype.Component
import java.io.Writer
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.Date
import kotlin.io.path.createTempDirectory
import kotlin.io.path.pathString

@Component
class BatchUtils {

  private val zoneOffset = ZoneOffset.UTC

  fun parseLocalDateToDate(date: LocalDate): Date {
    // convert the input date into a timestamp zoned in UTC
    // (for converting LocalDates in request params to batch job params
    return Date.from(date.atStartOfDay().atOffset(zoneOffset).toInstant())
  }

  fun parseDateToOffsetDateTime(date: Date): OffsetDateTime {
    // (for converting Dates in batch job params to sql query params)
    return date.toInstant().atOffset(zoneOffset)
  }

  private fun <T : Any> csvFileWriterBase(
    name: String,
    resource: WritableResource,
    headers: List<String>,
  ): FlatFileItemWriterBuilder<T> = FlatFileItemWriterBuilder<T>()
    .name(name)
    .resource(resource)
    .headerCallback(HeaderWriter(headers.joinToString(",")))

  fun <T : Any> csvFileWriter(
    name: String,
    resource: WritableResource,
    headers: List<String>,
    fields: List<String>,
  ): FlatFileItemWriter<T> = csvFileWriterBase<T>(name, resource, headers)
    .lineAggregator(CsvLineAggregator(fields))
    .build()

  fun <T : Any> recursiveCollectionCsvFileWriter(
    name: String,
    resource: WritableResource,
    headers: List<String>,
    fields: List<String>,
  ): FlatFileItemWriter<Collection<T>> = csvFileWriterBase<Collection<T>>(name, resource, headers)
    .lineAggregator(
      RecursiveCollectionLineAggregator<T>().apply {
        setDelegate(CsvLineAggregator(fields))
      },
    )
    .build()
}

class HeaderWriter(private val header: String) : FlatFileHeaderCallback {
  override fun writeHeader(writer: Writer) {
    writer.write(header)
  }
}

class TimestampIncrementer : JobParametersIncrementer {
  override fun getNext(inputParams: JobParameters?): JobParameters {
    val params = inputParams ?: JobParameters()

    return if (params.getParameter("timestamp") != null) {
      params
    } else {
      JobParametersBuilder(params)
        .addLong("timestamp", Instant.now().epochSecond)
        .toJobParameters()
    }
  }
}

class OutputPathIncrementer : JobParametersIncrementer {
  override fun getNext(inputParams: JobParameters?): JobParameters {
    val params = inputParams ?: JobParameters()

    return if (params.getParameter("outputPath") != null) {
      params
    } else {
      JobParametersBuilder(params)
        .addString("outputPath", createTempDirectory().pathString)
        .toJobParameters()
    }
  }
}

class CsvLineAggregator<T : Any>(
  fieldsToExtract: List<String>,
) : ExtractorLineAggregator<T>() {

  private val csvFormat: CSVFormat = CSVFormat.DEFAULT.builder()
    .setRecordSeparator("").get()

  init {
    setFieldExtractor(
      BeanWrapperFieldExtractor<T>().apply {
        setNames(fieldsToExtract.toTypedArray())
      },
    )
  }

  override fun doAggregate(fields: Array<out Any>): String {
    val out = StringBuilder()
    csvFormat.printRecord(out, *fields)
    return out.toString()
  }
}

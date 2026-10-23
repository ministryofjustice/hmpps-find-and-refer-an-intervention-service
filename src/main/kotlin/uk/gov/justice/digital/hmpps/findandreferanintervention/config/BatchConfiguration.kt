package uk.gov.justice.digital.hmpps.findandreferanintervention.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.batch.autoconfigure.BatchTaskExecutor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
class BatchConfiguration(
  @Value("\${spring.batch.concurrency.pool-size}") private val poolSize: Int,
  @Value("\${spring.batch.concurrency.queue-size}") private val queueSize: Int,
) {

  @Bean
  @BatchTaskExecutor
  fun batchTaskExecutor(): ThreadPoolTaskExecutor = ThreadPoolTaskExecutor().apply {
    corePoolSize = poolSize
    queueCapacity = queueSize
  }
}

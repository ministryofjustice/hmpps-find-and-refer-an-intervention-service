package uk.gov.justice.digital.hmpps.findandreferanintervention.config

import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

/*@EnableBatchProcessing(
  /*dataSourceRef = "mainDataSource",
  transactionManagerRef = "batchTransactionManager",
  databaseType = "H2",
  isolationLevelForCreate = "ISOLATION_READ_COMMITTED",
 */
)*/
@Configuration
@EnableTransactionManagement
class BatchConfiguration(
  @Value("\${spring.batch.concurrency.pool-size}") private val poolSize: Int,
  @Value("\${spring.batch.concurrency.queue-size}") private val queueSize: Int,
) {

  @Bean("asyncJobLauncher")
  fun asyncJobLauncher(jobRepository: JobRepository): JobLauncher {
    val taskExecutor = ThreadPoolTaskExecutor()
    taskExecutor.corePoolSize = poolSize
    taskExecutor.queueCapacity = queueSize
    taskExecutor.afterPropertiesSet()

    val launcher = TaskExecutorJobLauncher()
    launcher.setJobRepository(jobRepository)
    launcher.setTaskExecutor(taskExecutor)
    launcher.afterPropertiesSet()
    return launcher
  }

//  @Bean("batchJobBuilder")
//  fun batchJobBuilder(jobRepository: JobRepository): JobBuilder = JobBuilder("batchJobBuilder", jobRepository)

//  @Bean("batchStepBuilder")
//  fun batchStepBuilder(jobRepository: JobRepository): StepBuilder = StepBuilder("batchStepBuilder", jobRepository)

  @Bean("batchJobExplorer")
  fun jobExplorer(
    @Qualifier("mainDataSource") dataSource: DataSource,
    @Qualifier("transactionManager") batchTransactionManager: PlatformTransactionManager,
  ): JobExplorer {
    val factory = JobExplorerFactoryBean()
    factory.setDataSource(dataSource)
    factory.transactionManager = batchTransactionManager
    factory.afterPropertiesSet()
    return factory.getObject()
  }
}

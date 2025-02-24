package uk.gov.justice.digital.hmpps.findandreferanintervention.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
class DataSourceConfig(
  @Value("\${spring.datasource.url}") private val dataSourceUrl: String,
) {

  @Bean(name = ["mainDataSource", "dataSource"])
  @Primary
  fun dataSource(): DataSource = DataSourceBuilder.create()
    .driverClassName("org.postgresql.Driver")
    .url(dataSourceUrl)
    .build()

/*  @Bean("memoryDataSource")
  fun memoryDataSource(): DataSource = EmbeddedDatabaseBuilder()
    .setType(EmbeddedDatabaseType.H2)
    .addScript("/org/springframework/batch/core/schema-drop-h2.sql")
    .addScript("/org/springframework/batch/core/schema-h2.sql")
    .build()

  @Bean("batchTransactionManager")
  fun batchTransactionManager(@Qualifier("memoryDataSource") dataSource: DataSource): PlatformTransactionManager = JpaTransactionManager()
*/
  @Bean("transactionManager")
  @Primary
  fun transactionManager(): PlatformTransactionManager = JpaTransactionManager()
}

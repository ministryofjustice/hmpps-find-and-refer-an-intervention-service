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

  @Bean("transactionManager")
  @Primary
  fun transactionManager(): PlatformTransactionManager = JpaTransactionManager()
}

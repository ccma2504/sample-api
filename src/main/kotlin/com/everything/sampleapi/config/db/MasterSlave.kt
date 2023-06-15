package com.everything.sampleapi.config.db

import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import java.util.*
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
        basePackages = [MasterSlave.PACKAGE_ROOT],
        entityManagerFactoryRef = MasterSlave.ENTITY_MANAGER,
        transactionManagerRef = MasterSlave.TRANSACTION_MANAGER
)
class MasterSlave {
    companion object {
        const val PACKAGE_ROOT = "com.everything.investment"
        const val MASTER_PROP_PREFIX = "datasource.master"
        const val MASTER_DATA_SOURCE = "master-datasource"
        const val SLAVE_PROP_PREFIX = "datasource.slave"
        const val SLAVE_DATA_SOURCE = "slave-datasource"
        const val ROUTING_DATA_SOURCE = "routing-datasource"
        const val ENTITY_MANAGER = "entity-manager"
        const val TRANSACTION_MANAGER = "transaction-manager"
    }

    @Primary
    @Bean(ENTITY_MANAGER)
    fun entityManager(@Qualifier(ROUTING_DATA_SOURCE) dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        return LocalContainerEntityManagerFactoryBean().apply {
            this.dataSource = dataSource

            setPackagesToScan(PACKAGE_ROOT)

            Properties().apply {
                put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
            }.also {
                setJpaProperties(it)
            }

            jpaVendorAdapter = HibernateJpaVendorAdapter()
        }
    }

    @Primary
    @Bean(value = [MASTER_DATA_SOURCE])
    @ConfigurationProperties(prefix = MASTER_PROP_PREFIX)
    fun masterDataSource(): DataSource {
        return DataSourceBuilder.create().type(BasicDataSource::class.java).build()
    }

    @Bean(value = [SLAVE_DATA_SOURCE])
    @ConfigurationProperties(prefix = SLAVE_PROP_PREFIX)
    fun slaveDataSource(): DataSource {
        return DataSourceBuilder.create().type(BasicDataSource::class.java).build()
    }

    @Bean(ROUTING_DATA_SOURCE)
    fun routingDataSource(@Qualifier(SLAVE_DATA_SOURCE) slaveDataSource: DataSource,
                          @Qualifier(MASTER_DATA_SOURCE) masterDataSource: DataSource): DataSource {
        return RoutingDataSource().apply {
            setDefaultTargetDataSource(masterDataSource)
            setTargetDataSources(
                    hashMapOf<Any, Any>(
                            RoutingDataSource.SLAVE to slaveDataSource,
                            RoutingDataSource.MASTER to masterDataSource
                    )
            )
            afterPropertiesSet()
        }.let {
            LazyConnectionDataSourceProxy(it)
        }
    }

    @Primary
    @Bean(TRANSACTION_MANAGER)
    fun transactionManager(
            @Qualifier(ENTITY_MANAGER) entityManager: LocalContainerEntityManagerFactoryBean
    ): PlatformTransactionManager {
        return JpaTransactionManager().apply {
            entityManagerFactory = entityManager.`object`
        }
    }
}
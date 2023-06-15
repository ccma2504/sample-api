package com.everything.sampleapi.config.db

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager

class RoutingDataSource : AbstractRoutingDataSource() {
    companion object {
        const val MASTER = "master"
        const val SLAVE = "slave"
    }

    override fun determineCurrentLookupKey(): String? {
        return SLAVE.takeIf { TransactionSynchronizationManager.isCurrentTransactionReadOnly() }
    }
}
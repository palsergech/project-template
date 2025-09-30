package io.github.palsergech.lib.spring.transaction

import kotlinx.coroutines.runBlocking
import org.springframework.transaction.support.TransactionSynchronizationManager
import org.springframework.transaction.support.TransactionTemplate
import io.github.palsergech.lib.platform.transaction.TransactionService

class DefaultBlockingTransactionService(
    private val transactionTemplate: TransactionTemplate
): TransactionService {

    private data class TransactionResultWrapper<T>(val result: T)

    override suspend fun coTransactionExists(): Boolean {
        return TransactionSynchronizationManager.isActualTransactionActive()
    }

    override suspend fun <T> coInTransaction(block: suspend () -> T): T {
        val result = transactionTemplate
            .execute {
                TransactionResultWrapper(runBlocking { block() })
            }
            ?: throw IllegalStateException("Transaction result is null")
        return result.result
    }

    override fun transactionExists(): Boolean {
        return TransactionSynchronizationManager.isActualTransactionActive()
    }

    override fun <T> inTransaction(block: suspend () -> T): T {
        return runBlocking {
            coInTransaction {
                block()
            }
        }
    }
}

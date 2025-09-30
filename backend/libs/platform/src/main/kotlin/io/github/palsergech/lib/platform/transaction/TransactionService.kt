package io.github.palsergech.lib.platform.transaction

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking

interface TransactionService {
    suspend fun coTransactionExists(): Boolean
    suspend fun <T> coInTransaction(block: suspend () -> T): T

    fun transactionExists(): Boolean
    fun <T> inTransaction(block: suspend () -> T): T
}

fun <T> Flow<T>.inTransaction(transactionService: TransactionService): Flow<T> {
    val flow = this
    return runBlocking {
        transactionService.coInTransaction {
            flow.toList().asFlow()
        }
    }
}
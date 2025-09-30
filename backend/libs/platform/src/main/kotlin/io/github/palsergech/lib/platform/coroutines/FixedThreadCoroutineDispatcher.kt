package io.github.palsergech.lib.platform.coroutines

import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger


private class FixedThreadCoroutineDispatcher(private val prefix: String) : ThreadFactory {
    private val threadNumber = AtomicInteger(1)

    override fun newThread(r: Runnable): Thread {
        val threadName = "$prefix-${threadNumber.getAndIncrement()}"
        return Thread(r, threadName)
    }
}

fun createFixedThreadCoroutineDispatcher(name: String, nThreads: Int) =
    Executors.newFixedThreadPool(nThreads, FixedThreadCoroutineDispatcher(name)).asCoroutineDispatcher()

package io.github.palsergech.lib.spring.scheduling

import com.github.kagkarlsson.scheduler.task.FailureHandler
import com.github.kagkarlsson.scheduler.task.Task
import com.github.kagkarlsson.scheduler.task.helper.Tasks
import com.github.kagkarlsson.scheduler.task.schedule.Schedule
import com.github.kagkarlsson.scheduler.task.schedule.Schedules.daily
import com.github.kagkarlsson.scheduler.task.schedule.Schedules.fixedDelay
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import java.time.LocalTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

inline fun <reified T> oneTimeTask(
    name: String,
    logger: Logger,
    failureRetryDelay: Duration = 1.minutes,
    crossinline task: suspend (T) -> Unit
): Task<T> = Tasks
    .oneTime(name, T::class.java)
    .onFailure(FailureHandler.OnFailureRetryLater(failureRetryDelay.toJavaDuration()))
    .execute { instance, _ ->
        try {
            logger.info("executing one-time task $instance")
            runBlocking { task(instance.data) }
            logger.info("successfully executed one-time task $instance")
        } catch (e: Exception) {
            logger.error("error executing one-time task $instance", e)
            throw e
        }
    }

fun persistentRecurringTask(
    name: String,
    fixedDelay: Duration,
    logger: Logger,
    task: suspend () -> Unit
): Task<Void> = persistentRecurringTask(name, fixedDelay(fixedDelay.toJavaDuration()), logger, task)

fun persistentDailyTask(
    name: String,
    localTime: LocalTime,
    logger: Logger,
    task: suspend () -> Unit
): Task<Void> = persistentRecurringTask(name, daily(localTime), logger, task)

private fun persistentRecurringTask(
    name: String,
    schedule: Schedule,
    logger: Logger,
    task: suspend () -> Unit
): Task<Void> = Tasks
    .recurring(name, schedule)
    .execute { instance, _ ->
        try {
            logger.info("executing recurring task $instance")
            runBlocking { task() }
            logger.info("successfully executed recurring task $instance")
        } catch (e: Exception) {
            logger.error("error executing recurring task $instance", e)
        }
    }

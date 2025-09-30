package io.github.palsergech.lib.spring.scheduling

import com.github.kagkarlsson.scheduler.Scheduler
import com.github.kagkarlsson.scheduler.task.SchedulableInstance
import com.github.kagkarlsson.scheduler.task.TaskDescriptor
import com.github.kagkarlsson.scheduler.task.TaskInstanceId
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*

class ScheduleTasks(
    val scheduler: Scheduler
) {

    val logger = LoggerFactory.getLogger(javaClass)

    inline fun <reified T> scheduleParametrizedOneTimeTask(
        name: String,
        data: T,
        scheduledTo: Instant = Instant.now()
    ) {
        val taskInstance = TaskDescriptor
            .of(name, T::class.java)
            .instance(UUID.randomUUID().toString())
            .data(data)
            .scheduledTo(scheduledTo)

        scheduler.schedule(taskInstance)
        logger.info("scheduled parametrized one-time task $taskInstance to $scheduledTo")
    }

    fun scheduleOneTimeTask(
        name: String,
        instanceId: String? = UUID.randomUUID().toString(),
        scheduledTo: Instant
    ): SchedulableInstance<Void> {
        val taskInstance = TaskDescriptor
            .of(name)
            .instance(instanceId ?: UUID.randomUUID().toString())
            .scheduledTo(scheduledTo)

        scheduler.schedule(taskInstance)
        logger.info("scheduled one-time task $taskInstance to $scheduledTo")
        return taskInstance
    }

    fun cancelOneTimeTask(taskName: String, taskInstanceId: String) {
        scheduler.cancel(TaskInstanceId.of(taskName, taskInstanceId))
        logger.info("cancelled one-time task $taskName with instanceId $taskInstanceId")
    }
}

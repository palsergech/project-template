package io.github.palsergech.lib.platform.utils

import org.slf4j.Logger


fun <R> namedAction(logger: Logger, action: String, f: () -> R): R {
    return try {
        f()
    } catch (e: Exception) {
        logger.error("action '$action' failed", e)
        throw e
    }
}

suspend fun <R> coNamedAction(logger: Logger, action: String, f: suspend () -> R): R {
    return try {
        f()
    } catch (e: Exception) {
        logger.error("action '$action' failed", e)
        throw e
    }
}
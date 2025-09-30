package io.github.palsergech.lib.spring.rest

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.resource.NoResourceFoundException
import io.github.palsergech.lib.platform.domain.DomainError
import io.github.palsergech.lib.platform.domain.ObjectNotFoundError
import org.springframework.security.access.AccessDeniedException

@ControllerAdvice
class DefaultExceptionHandler {

    private val logger = LoggerFactory.getLogger(DefaultExceptionHandler::class.java)

    data class ErrorDTO(val message: String)

    @ExceptionHandler(value = [Exception::class])
    fun onUnhandledException(ex: Exception): ResponseEntity<ErrorDTO> {
        logger.error("Unhandled exception occurred. Reason: ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDTO(ex.message ?: "unknown error"))
    }

    @ExceptionHandler(value = [DomainError::class])
    fun onDomainError(ex: DomainError): ResponseEntity<ErrorDTO> {
        logger.warn("DomainError. Reason: ${ex.message}", ex)
        return when(ex) {
            is ObjectNotFoundError ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorDTO(ex.message))
            else ->
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO(ex.message))
        }
    }

    @ExceptionHandler(value = [NotImplementedError::class])
    fun onNotImplemented(ex: NotImplementedError, response: HttpServletResponse) {
        logger.error("NotImplementedError. Reason: ${ex.message}", ex)
        response.sendError(HttpStatus.NOT_IMPLEMENTED.value(), ex.message)
    }

    @ExceptionHandler(value = [MissingServletRequestParameterException::class])
    fun onMissingServletRequestParameter(ex: MissingServletRequestParameterException): ResponseEntity<ErrorDTO> {
        logger.warn("MissingServletRequestParameterException. Reason: ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO(ex.message ?: ""))
    }

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun onRequestBodyParsingError(ex: HttpMessageNotReadableException): ResponseEntity<ErrorDTO> {
        logger.warn("failed to parse request body", ex)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO(ex.message ?: ""))
    }

    @ExceptionHandler(value = [NoResourceFoundException::class])
    fun onNotFound(ex: NoResourceFoundException): ResponseEntity<ErrorDTO> {
        logger.warn("resource not found ${ex.httpMethod} ${ex.resourcePath}")
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorDTO(
            "resource not found ${ex.httpMethod} ${ex.resourcePath}"
        ))
    }

    @ExceptionHandler(value = [AccessDeniedException::class])
    fun onAccessDenied(ex: AccessDeniedException): ResponseEntity<ErrorDTO> {
        logger.error("access denied ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorDTO(ex.message ?: ""))
    }
    
    @ExceptionHandler(value = [HttpRequestMethodNotSupportedException::class])
    fun onMethodNotSupported(req: HttpServletRequest, ex: HttpRequestMethodNotSupportedException): ResponseEntity<ErrorDTO> {
        logger.warn("method not supported ${req.method} ${req.requestURI}")
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ErrorDTO(
            "method ${ex.method} not supported"
        ))
    }
}

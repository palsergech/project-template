package io.github.palsergech.rest

import io.github.palsergech.lib.platform.domain.DomainError
import io.github.palsergech.lib.platform.domain.ObjectNotFoundError
import io.github.palsergech.rest.dto.*
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.resource.NoResourceFoundException

@ControllerAdvice
internal class ApiExceptionHandler {

    private val logger = LoggerFactory.getLogger(ApiExceptionHandler::class.java)

    @ExceptionHandler(value = [Exception::class])
    fun onUnhandledException(ex: Exception): ResponseEntity<ServerErrorDTO> {
        logger.error("Unhandled exception occurred. Reason: ${ex.message}", ex)
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
            .body(ServerErrorDTO(
                type = ServerErrorDTO.Type.SERVER_ERROR,
                message = "internal server error"
            ))
    }

    @ExceptionHandler(value = [DomainError::class])
    fun onDomainError(ex: DomainError): ResponseEntity<*> {
        logger.warn("DomainError. Reason: ${ex.message}", ex)
        return when(ex) {
            is ObjectNotFoundError ->
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(NotFoundErrorDTO(
                        type = NotFoundErrorDTO.Type.NOT_FOUND_ERROR,
                        details = NotFoundErrorDTODetails(
                            objectType = ex.objectType,
                            key = ex.key.toString()
                        )
                    ))
            else ->
                ResponseEntity.status(BAD_REQUEST)
                    .body(DomainErrorDTO(
                        type = DomainErrorDTO.Type.DOMAIN_ERROR,
                        message = ex.message,
                        details = null
                    ))
        }
    }

    @ExceptionHandler(value = [MissingServletRequestParameterException::class])
    fun onMissingServletRequestParameter(ex: MissingServletRequestParameterException): ResponseEntity<BadRequestErrorDTO> {
        logger.warn("MissingServletRequestParameterException. Reason: ${ex.message}", ex)
        return ResponseEntity.status(BAD_REQUEST)
            .body(BadRequestErrorDTO(
                type = BadRequestErrorDTO.Type.BAD_REQUEST,
                message = "invalid request format"
            ))
    }

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun onRequestBodyParsingError(ex: HttpMessageNotReadableException): ResponseEntity<BadRequestErrorDTO> {
        logger.warn("failed to parse request body", ex)
        return ResponseEntity.status(BAD_REQUEST)
            .body(BadRequestErrorDTO(
                type = BadRequestErrorDTO.Type.BAD_REQUEST,
                message = "invalid request format: ${ex.message}"
            ))
    }

    @ExceptionHandler(value = [NoResourceFoundException::class])
    fun onNotFound(ex: NoResourceFoundException): ResponseEntity<NotFoundErrorDTO> {
        logger.warn("resource not found ${ex.httpMethod} ${ex.resourcePath}")
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NotFoundErrorDTO(
            type = NotFoundErrorDTO.Type.NOT_FOUND_ERROR,
            details = NotFoundErrorDTODetails(
                objectType = "endpoint",
                key = "${ex.httpMethod} ${ex.resourcePath}"
            )
        ))
    }

    @ExceptionHandler(value = [HttpRequestMethodNotSupportedException::class])
    fun onMethodNotSupported(req: HttpServletRequest, ex: HttpRequestMethodNotSupportedException): ResponseEntity<BadRequestErrorDTO> {
        logger.warn("method not supported ${req.method} ${req.requestURI}")
        return ResponseEntity.status(METHOD_NOT_ALLOWED).body(BadRequestErrorDTO(
            type = BadRequestErrorDTO.Type.BAD_REQUEST,
            message = "method ${ex.method} not supported"
        ))
    }

    @ExceptionHandler(value = [AccessDeniedException::class])
    fun onAccessDenied(ex: AccessDeniedException): ResponseEntity<AuthErrorDTO> {
        logger.error("access denied ${ex.message}", ex)
        return ResponseEntity.status(FORBIDDEN)
            .body(AuthErrorDTO(
                type = AuthErrorDTO.Type.AUTH_ERROR,
                message = ex.message ?: "access denied"
            ))
    }
}

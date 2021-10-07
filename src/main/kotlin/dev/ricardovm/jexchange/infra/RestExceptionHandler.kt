package dev.ricardovm.jexchange.infra

import dev.ricardovm.jexchange.domain.JExchangeException
import org.jboss.logging.Logger
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

private val LOG: Logger = Logger.getLogger(DefaultExceptionMapper::class.java)

@Provider
class JExchangeExceptionMapper : ExceptionMapper<JExchangeException> {
    override fun toResponse(exception: JExchangeException): Response {
        LOG.warn("${exception.message} ${exception.userMessage}", exception)
        return Response.serverError().entity(exception.userMessage).build()
    }
}

@Provider
class WebApplicationExceptionMapper : ExceptionMapper<WebApplicationException> {
    override fun toResponse(exception: WebApplicationException): Response {
        LOG.warn(exception.message, exception)
        return Response.status(exception.response.status).entity(exception.message).build()
    }
}

@Provider
class DefaultExceptionMapper : ExceptionMapper<Exception> {
    override fun toResponse(exception: Exception): Response {
        LOG.error("Unknown error", exception)
        return Response.serverError().entity("Unknown error. Try again later.").build()
    }
}

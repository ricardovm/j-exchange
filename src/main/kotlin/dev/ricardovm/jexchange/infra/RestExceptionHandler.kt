package dev.ricardovm.jexchange.infra

import dev.ricardovm.jexchange.domain.JExchangeException
import io.quarkus.logging.Log
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class JExchangeExceptionMapper : ExceptionMapper<JExchangeException> {
    override fun toResponse(exception: JExchangeException): Response {
        Log.warn("${exception.message} ${exception.userMessage}", exception)
        return Response.serverError().entity(exception.userMessage).build()
    }
}

@Provider
class WebApplicationExceptionMapper : ExceptionMapper<WebApplicationException> {
    override fun toResponse(exception: WebApplicationException): Response {
        Log.warn(exception.message, exception)
        return Response.status(exception.response.status).entity(exception.message).build()
    }
}

@Provider
class DefaultExceptionMapper : ExceptionMapper<Exception> {
    override fun toResponse(exception: Exception): Response {
        Log.error("Unknown error", exception)
        return Response.serverError().entity("Unknown error. Try again later.").build()
    }
}

package dev.ricardovm.jexchange.domain

class JExchangeException(val userMessage: String, cause: Throwable?) : RuntimeException(cause)
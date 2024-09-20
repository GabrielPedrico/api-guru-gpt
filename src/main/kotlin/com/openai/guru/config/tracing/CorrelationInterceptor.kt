package com.openai.guru.config.tracing

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class CorrelationInterceptor : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        // Extrai o correlationId do header da requisição
        val correlationId = request.getHeader("X-Correlation-ID") ?: generateCorrelationId()

        // Define o correlationId e spanId no MDC (Mapeamento para o Sleuth)
        MDC.put("CORRELATION-ID", correlationId)
        MDC.put("SPAN-ID", generateSpanId())

        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        MDC.clear() // Limpa os valores do MDC após a requisição ser finalizada
    }

    // Função para gerar um correlationId se não vier no header
    private fun generateCorrelationId(): String {
        return java.util.UUID.randomUUID().toString()
    }

    // Função para gerar um spanId novo para cada requisição
    private fun generateSpanId(): String {
        return java.util.UUID.randomUUID().toString().substring(0, 8)
    }
}

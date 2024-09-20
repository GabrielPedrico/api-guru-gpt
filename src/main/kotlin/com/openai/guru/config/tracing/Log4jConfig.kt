package com.openai.guru.config.tracing

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class Log4jConfig {
    @Bean
    fun logger(injectionPoint: org.springframework.beans.factory.InjectionPoint): Logger {
        return LoggerFactory.getLogger(
            injectionPoint.methodParameter?.containingClass
                ?: injectionPoint.field?.declaringClass
                ?: Log4jConfig::class.java,
        )
    }
}

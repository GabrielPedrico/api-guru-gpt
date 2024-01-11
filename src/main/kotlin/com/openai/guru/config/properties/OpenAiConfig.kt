package com.openai.guru.config.properties

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(OpenAIProperties::class)
class OpenAiConfig {
}
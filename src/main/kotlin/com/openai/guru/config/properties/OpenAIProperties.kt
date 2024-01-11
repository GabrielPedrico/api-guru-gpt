package com.openai.guru.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "openai")
class OpenAIProperties (var api : String,
                        var assistant: String,
                        var token: String,
                        var header: String) {
}
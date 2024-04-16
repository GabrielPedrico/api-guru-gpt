package com.openai.guru.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
class Properties(
    var openai: String,
    var assistant: String,
    var token: String,
    var header: String,
    var payment:String
)
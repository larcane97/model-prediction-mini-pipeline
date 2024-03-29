package com.example.spring_gateway.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.DefaultUriBuilderFactory


@Configuration
class RestTemplateConfig {
    @Value("\${restTemplate.baseUrl}")
    private val baseUrl: String = "http://mini-pipeline-ms-svc:80"

    @Bean
    fun restTemplate(): RestTemplate {
        val restTemplate = RestTemplate();
        restTemplate.uriTemplateHandler = DefaultUriBuilderFactory(baseUrl);
        return restTemplate

    }
}
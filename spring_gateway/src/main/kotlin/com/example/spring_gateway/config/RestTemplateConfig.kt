package com.example.spring_gateway.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.DefaultUriBuilderFactory


@Configuration
class RestTemplateConfig {
    companion object{
        @Bean
        fun restTemplate():RestTemplate{
            val restTemplate = RestTemplate();
            restTemplate.uriTemplateHandler = DefaultUriBuilderFactory("http://test-model-server");
            return restTemplate
        }
    }
}
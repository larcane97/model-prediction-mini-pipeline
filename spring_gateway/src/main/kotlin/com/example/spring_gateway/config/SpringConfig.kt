package com.example.spring_gateway.config

import com.example.spring_gateway.repository.FareRecommendRepository
import com.example.spring_gateway.repository.RedisFareRecommendRepository
import com.example.spring_gateway.service.FareRecommendService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpringConfig {

    @Bean
    fun fareRecommendService():FareRecommendService{
        return FareRecommendService(fareRecommendRepository());
    }

    @Bean
    fun fareRecommendRepository(): FareRecommendRepository{
        return RedisFareRecommendRepository();
    }
}
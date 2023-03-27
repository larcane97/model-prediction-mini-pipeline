package com.example.spring_gateway.config

import com.example.spring_gateway.component.KeyGenerator
import com.example.spring_gateway.repository.FareRecommendRepository
import com.example.spring_gateway.repository.RedisFareRecommendRepository
import com.example.spring_gateway.service.FareRecommendService
import com.example.spring_gateway.service.FareRecommendServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.redis.core.RedisTemplate


//@Configuration
//class SpringConfig {
//    @Bean
//    fun keyGenerator() = KeyGenerator();
//
//    @Bean
//    fun fareRecommendRepository():FareRecommendRepository {
//        return RedisFareRecommendRepository(RedisConfig().redisTemplate())
//    }
//
//    @Bean
//    fun fareRecommendService():FareRecommendService {
//        return FareRecommendServiceImpl(fareRecommendRepository(), RestTemplateConfig().restTemplate(), keyGenerator())
//    }
//}
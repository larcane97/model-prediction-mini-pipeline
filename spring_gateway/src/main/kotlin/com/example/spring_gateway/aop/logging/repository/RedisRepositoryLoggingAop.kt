package com.example.spring_gateway.aop.logging.repository

import com.example.spring_gateway.entity.exception.FeatureReadException
import com.example.spring_gateway.entity.feature.FareRecommendFeature
import com.example.spring_gateway.entity.feature.HistoricalFeature
import com.example.spring_gateway.entity.feature.RealTimeFeature
import com.example.spring_gateway.repository.RedisFareRecommendRepository
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class RedisRepositoryLoggingAop {

    @Around("execution(* com.example.spring_gateway.repository.RedisFareRecommendRepository.getHistoricalFeature(..))")
    @Throws(Throwable::class)
    fun getHistoricalFeatureLogging(pjp: ProceedingJoinPoint): Any {
        return try {
            pjp.proceed();
        } catch (ex: Exception) {
            throw FeatureReadException(featureType = "historical", key = (pjp.args[0]).toString())
        }
    }

    @Around("execution(* com.example.spring_gateway.repository.RedisFareRecommendRepository.getRealtimeFeature(..))")
    @Throws(Throwable::class)
    fun getRealtimeFeatureLogging(pjp: ProceedingJoinPoint): Any {
        return try {
            pjp.proceed();
        } catch (ex: Exception) {
            throw FeatureReadException(featureType = "realtime", key = (pjp.args[0]).toString())
        }
    }
}
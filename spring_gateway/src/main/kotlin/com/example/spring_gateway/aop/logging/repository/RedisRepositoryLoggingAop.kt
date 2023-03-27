package com.example.spring_gateway.aop.logging.repository

import com.example.spring_gateway.entity.exception.FeatureReadException
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
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
            val args = pjp.args
            if (args != null && args.isNotEmpty() && args[0] is List<*>) {
                val keys = args[0] as List<*>
                val key = keys[0].toString()
                throw FeatureReadException(featureType = "historical", key = key)
            } else
                throw FeatureReadException(featureType = "historical", key = "NOKEY")
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
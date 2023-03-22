package com.example.spring_gateway.aop.logging

import com.example.spring_gateway.entity.error.FeatureReadException
import com.example.spring_gateway.entity.feature.FareRecommendFeature
import com.example.spring_gateway.entity.feature.HistoricalFeature
import com.example.spring_gateway.entity.feature.RealTimeFeature
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class FareRecommendRepositoryLoggingAop {
    private val logger: Logger = LoggerFactory.getLogger(FareRecommendRepositoryLoggingAop::class.java)
    private val DEFAULT_HISTORICAL_FEATURE = HistoricalFeature(callCountAvgDuring2Week = 11, dispatchCountAvgDuring2Week = 11)
    private val DEFAULT_REALTIME_FEATURE = RealTimeFeature(callCountNow = 11, dispatchCountNow = 11)
    private val DEFAULT_RECOMMEND_FEATURE = FareRecommendFeature(DEFAULT_HISTORICAL_FEATURE,DEFAULT_REALTIME_FEATURE)

    @Around("execution(* com.example.spring_gateway.repository.*Repository.getFareRecommendFeature(..))")
    @Throws(Throwable::class)
    fun getFareRecommendFeatureLogging(pjp:ProceedingJoinPoint):Any{
        return try{
            pjp.proceed();
        }
        catch(ex:Exception){
            when(ex){
                is FeatureReadException ->{
                    logger.error("Can't get feature from repogisotry. DEFAULT variable would be used.")
                }
                else->{
                    logger.error("Unknown error. DEFAULT variable would be used.")
                }
            }
            DEFAULT_RECOMMEND_FEATURE
        }
    }
}
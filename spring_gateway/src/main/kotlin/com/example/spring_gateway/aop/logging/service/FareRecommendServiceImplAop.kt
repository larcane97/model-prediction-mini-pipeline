package com.example.spring_gateway.aop.logging.service

import com.example.spring_gateway.dto.FareRecommendDto
import com.example.spring_gateway.dto.request.FareRecommendRequest
import com.example.spring_gateway.dto.response.FareRecommendResponse
import com.example.spring_gateway.entity.exception.FeatureReadException
import com.example.spring_gateway.entity.exception.IllegalMSResponseException
import com.example.spring_gateway.entity.exception.MSConnectionException
import com.example.spring_gateway.entity.feature.FareRecommendFeature
import com.example.spring_gateway.entity.feature.HistoricalFeature
import com.example.spring_gateway.entity.feature.RealTimeFeature
import com.example.spring_gateway.service.FareRecommendServiceImpl
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class FareRecommendServiceImplAop {
    val logger: Logger = LoggerFactory.getLogger(FareRecommendServiceImplAop::class.java)
    private val DEFAULT_HISTORICAL_FEATURE = HistoricalFeature(callCountAvg = 1)
    private val DEFAULT_REALTIME_FEATURE = RealTimeFeature(callCountNow = 1)
    private val DEFAULT_RECOMMEND_FEATURE = FareRecommendFeature(DEFAULT_HISTORICAL_FEATURE, DEFAULT_REALTIME_FEATURE)
    private val DEFAULT_RECOMMEND_FARE = 10000L

    @Around("execution(* com.example.spring_gateway.service.FareRecommendService.getRecommendFare(..))")
    @Throws(Throwable::class)
    fun getRecommendFareAop(pjp: ProceedingJoinPoint): Any? {
        return try {
            pjp.proceed();
        } catch (ex: Exception) {
            when (ex) {
                is FeatureReadException -> {
                    if(ex.featureType == "historical") {
                        logger.error("Can't get historical feature from redis. try to get data through previous key..(Not implemented yet.. DEFAULT variable would be used.)")
                        FareRecommendResponse(
                            requestId = (pjp.args[0] as FareRecommendRequest).requestId, recommendFare = DEFAULT_RECOMMEND_FARE
                        )
                    }
                    else {
                        logger.error("Can't get realtime feature from redis. try to get data through previous key..(Not implemented yet.. DEFAULT variable would be used.)")
                        FareRecommendResponse(
                            requestId = (pjp.args[0] as FareRecommendRequest).requestId, recommendFare = DEFAULT_RECOMMEND_FARE
                        )
                    }
                }
                is IllegalMSResponseException -> {
                    logger.error("Invalid response from model server. DEFAULT variable would be used.")
                    FareRecommendResponse(
                        requestId = ex.request.requestId, recommendFare = DEFAULT_RECOMMEND_FARE
                    )
                }

                is MSConnectionException -> {
                    logger.error("Can't connect to model server. DEFAULT variable would be used.")
                    FareRecommendResponse(
                        requestId = ex.request.requestId, recommendFare = DEFAULT_RECOMMEND_FARE
                    )
                }

                else -> {
                    logger.error("Unknown error. DEFAULT variable would be used.")
                    FareRecommendResponse(
                        requestId = (pjp.args[0] as FareRecommendRequest).requestId, recommendFare = DEFAULT_RECOMMEND_FARE
                    )
                }
            }
        }
    }
//
//    @Around("execution(* com.example.spring_gateway.service.FareRecommendServiceImpl.getHistoricalFeature(..))")
//    fun getHistoricalFeatureLogging(pjp: ProceedingJoinPoint): Any? {
//        return try {
//            pjp.proceed()
//        } catch (ex: Exception) {
//            try {
//                if (ex is FeatureReadException && ex.featureType == "historical") {
//                    logger.error("Can't get historical feature from redis. try to get data through previous key..")
//                    val service = pjp.target as FareRecommendServiceImpl
//                    val key = ex.key
//                    val prevHistoricalKey = service.keyGenerator.getPrevHistoricalKey(key)
//                    service.fareRecommendRepository.getHistoricalFeature(prevHistoricalKey)
//                } else DEFAULT_HISTORICAL_FEATURE
//            } catch (ex: Exception) {
//                DEFAULT_HISTORICAL_FEATURE
//            }
//        } catch (ex: AssertionError) {
//            println("\n\nit's assertionError !!!!!\n\n")
//        }
//    }
//
//    @Around("execution(* com.example.spring_gateway.service.FareRecommendServiceImpl.getRealTimeFeature(..))")
//    fun getRealtimeFeatureLogging(pjp: ProceedingJoinPoint): Any? {
//        return try {
//            pjp.proceed()
//        } catch (ex: Exception) {
//            try {
//                if (ex is FeatureReadException && ex.featureType == "realtime") {
//                    logger.error("Can't get realtime feature from redis. try to get data through previous key..")
//                    val service = pjp.target as FareRecommendServiceImpl
//                    val key = ex.key
//                    val realtimeKey = service.keyGenerator.getPrevRealtimeKey(key)
//                    service.fareRecommendRepository.getRealtimeFeature(realtimeKey)
//                } else DEFAULT_REALTIME_FEATURE
//            } catch (ex: Exception) {
//                DEFAULT_REALTIME_FEATURE
//            }
//        }
//    }

}
package com.example.spring_gateway.aop.logging.service

import com.example.spring_gateway.dto.request.FareRecommendRequest
import com.example.spring_gateway.dto.response.FareRecommendResponse
import com.example.spring_gateway.entity.exception.FeatureReadException
import com.example.spring_gateway.entity.exception.IllegalMSResponseException
import com.example.spring_gateway.entity.exception.MSConnectionException
import com.example.spring_gateway.entity.feature.FareRecommendFeature
import com.example.spring_gateway.entity.feature.HistoricalFeature
import com.example.spring_gateway.entity.feature.RealTimeFeature
import com.example.spring_gateway.service.FareRecommendService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
@Aspect
class FareRecommendServiceLoggingAop {
    val logger: Logger = LoggerFactory.getLogger(FareRecommendServiceLoggingAop::class.java)
    private val DEFAULT_HISTORICAL_FEATURE = HistoricalFeature(callCountAvg = 1)
    private val DEFAULT_REALTIME_FEATURE = RealTimeFeature(callCountNow = 1)
    private val DEFAULT_RECOMMEND_FEATURE = FareRecommendFeature(DEFAULT_HISTORICAL_FEATURE,DEFAULT_REALTIME_FEATURE)

    @Around("execution(* com.example.spring_gateway.service.FareRecommendService.getRecommendFare(..))")
    @Throws(Throwable::class)
    fun getRecommendFareLogging(pjp: ProceedingJoinPoint): Any? {
        return try {
            pjp.proceed();
        } catch(ex:Exception){
            when(ex){
                is FeatureReadException -> {
                    when(ex.featureType){
                        "historical" -> {
                            logger.error("Can't get historical feature from redis. try to get data through previous key..")
                            val service = pjp.target as FareRecommendService
                            val key = ex.key
                            service.historicalKey = service.keyGenerator.getPrevHistoricalKey(key)
                            pjp.proceed()
                        }
                        "realtime" -> {
                            logger.error("Can't get realtime feature from redis. try to get data through previous key..")
                            val service = pjp.target as FareRecommendService
                            val key = ex.key
                            service.realtimeKey = service.keyGenerator.getPrevRealtimeKey(key)
                            pjp.proceed()
                        }

                        else -> {
                            FareRecommendResponse(
                                requestId = (pjp.args[0] as FareRecommendRequest).requestId,
                                recommendFare = 1000
                            )
                        }
                    }
                }
                is IllegalMSResponseException -> {
                    logger.error("Invaild response from model server. DEFAULT variable would be used.")
                    FareRecommendResponse(
                        requestId = (pjp.args[0] as FareRecommendRequest).requestId,
                        recommendFare = 1000
                    )
                }
                is MSConnectionException -> {
                    logger.error("Can't connect to model server. DEFAULT variable would be used.")
                    FareRecommendResponse(
                        requestId = (pjp.args[0] as FareRecommendRequest).requestId,
                        recommendFare = 1000
                    )
                }
                else -> {
                    logger.error("unknown error. DEFAULT variable would be used.")
                    FareRecommendResponse(
                        requestId = (pjp.args[0] as FareRecommendRequest).requestId,
                        recommendFare = 1000
                    )
                }
            }
        }
    }

    fun getPrevFeature(ex: Exception){
        when(ex.message){
            "historical" -> {

            }
        }
    }
}
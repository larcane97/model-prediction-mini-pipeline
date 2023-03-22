package com.example.spring_gateway.aop.logging

import com.example.spring_gateway.dto.request.FareRecommendRequest
import com.example.spring_gateway.dto.response.FareRecommendResponse
import com.example.spring_gateway.entity.error.IllegalMSResponseException
import com.example.spring_gateway.entity.error.MSConnectionException
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

    @Around("execution(* com.example.spring_gateway.service.FareRecommendService.getRecommendFare(..))")
    @Throws(Throwable::class)
    fun getRecommendFareLogging(pjp: ProceedingJoinPoint): Any? {
        return try {
            pjp.proceed();
        } catch(ex:Exception){
            when(ex){
                is IllegalMSResponseException -> {
                    logger.error("Invaild response from model server. DEFAULT variable would be used.")
                }
                is MSConnectionException -> {
                    logger.error("Can't connect to model server. DEFAULT variable would be used.")
                }
                else -> {
                    logger.error("unknown error. DEFAULT variable would be used.")
                }
            }
            // return default vaule
            FareRecommendResponse(requestId = (pjp.args[0] as FareRecommendRequest).requestId, recommendFare = 1000)
        }
    }
}
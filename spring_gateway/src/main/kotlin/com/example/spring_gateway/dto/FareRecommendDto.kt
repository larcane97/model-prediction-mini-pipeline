package com.example.spring_gateway.dto

import com.example.spring_gateway.entity.enums.FareType

data class FareRecommendDto(
    val requestId:Long,
    val orgH3:String,
    val dstH3:String,
    val distance:Long,
    val eta:Long?,
    val callCountNow:Long?,
    val callCountAvg:Long?,
    val fareType: Int?
)
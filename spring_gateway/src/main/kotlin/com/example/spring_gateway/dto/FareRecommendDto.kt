package com.example.spring_gateway.dto

import com.example.spring_gateway.entity.enums.FareType

data class FareRecommendDto(
    val requestId:Long,
    val orgH3:String,
    val dstH3:String,
    val distance:Long,
    val eta:Long?,
    val callCountNow:Long?,
    val dispatchCountNow:Long?,
    val callCountAvgDuring2Week:Long?,
    val dispatchCountAvgDuring2Week:Long?,
    val fareType: Int?
)
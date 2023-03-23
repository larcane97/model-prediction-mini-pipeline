package com.example.spring_gateway.dto.request

import com.example.spring_gateway.entity.enums.FareType

data class FareRecommendRequest(
    val requestId: Long,
    val orgLat: Double,
    val orgLot: Double,
    val dstLat: Double,
    val dstLot: Double,
    val distance: Long,
    val eta: Long?,
    val fareType: Int?
)
package com.example.spring_gateway.dto.request

import com.example.spring_gateway.entity.enums.FareType

class FareRecommendRequest(
    var requestId: Long,
    var orgLat: Double,
    var orgLot: Double,
    var dstLat: Double,
    var dstLot: Double,
    var distance: Long,
    var eta: Long?,
    var fareType: Int?
)
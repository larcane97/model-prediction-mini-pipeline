package com.example.spring_gateway.dto.request

import com.example.spring_gateway.entity.enums.FareType

class FareRecommendRequest(
    var requestId:Long,
    var org_lat:Double,
    var org_lot:Double,
    var dst_lat:Double,
    var dst_lot:Double,
    var distance:Long,
    var eta:Long?,
    var fare_type: FareType?
)
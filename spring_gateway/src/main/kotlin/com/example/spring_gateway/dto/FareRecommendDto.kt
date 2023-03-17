package com.example.spring_gateway.dto

import com.example.spring_gateway.entity.enums.FareType

class FareRecommendDto(
    var requestId:Long,
    var orgH3:String,
    var dstH3:String,
    var distance:Long,
    var eta:Long?,
    var callCountNow:Long?,
    var dispatchCountNow:Long?,
    var callCountAvgDuring2Week:Long?,
    var dispatchCountAvgDuring2Week:Long?,
    var fareType: FareType?
)
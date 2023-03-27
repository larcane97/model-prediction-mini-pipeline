package com.example.spring_gateway.entity.exception

import com.example.spring_gateway.dto.FareRecommendDto

class MSConnectionException(val request: FareRecommendDto) : Exception()
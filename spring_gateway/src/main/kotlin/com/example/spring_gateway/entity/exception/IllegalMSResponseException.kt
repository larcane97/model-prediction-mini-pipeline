package com.example.spring_gateway.entity.exception

import com.example.spring_gateway.dto.FareRecommendDto

class IllegalMSResponseException(val request: FareRecommendDto):Exception() {
}
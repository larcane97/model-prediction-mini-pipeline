package com.example.spring_gateway.service

import com.example.spring_gateway.dto.FareRecommendDto
import com.example.spring_gateway.dto.request.FareRecommendRequest
import com.example.spring_gateway.dto.response.FareRecommendResponse
import com.example.spring_gateway.entity.feature.FareRecommendFeature
import com.example.spring_gateway.entity.feature.HistoricalFeature
import org.springframework.stereotype.Component

interface FareRecommendService {
    fun getRecommendFare(request: FareRecommendRequest): FareRecommendResponse;
    fun getFareRecommendFeature(historicalKey: List<String>, realtimeKey: String): FareRecommendFeature;
    fun getRecommendFareFromModel(request: FareRecommendDto, url: String): FareRecommendResponse
    fun getFareRecommendDto(
        request: FareRecommendRequest, orgH3: String, dstH3: String, feature: FareRecommendFeature
    ): FareRecommendDto

}
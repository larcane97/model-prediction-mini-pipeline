package com.example.spring_gateway.service

import com.example.spring_gateway.dto.FareRecommendDto
import com.example.spring_gateway.entity.feature.FareRecommendFeature
import com.example.spring_gateway.dto.request.FareRecommendRequest
import com.example.spring_gateway.dto.response.FareRecommendResponse
import com.example.spring_gateway.repository.FareRecommendRepository
import com.uber.h3core.H3Core
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.lang.IllegalStateException

class FareRecommendService(
    @Autowired val fareRecommendRepository: FareRecommendRepository,
    @Autowired val restTemplate: RestTemplate
) {
    private val h3Core = H3Core.newInstance()

    fun getRecommendFare(request: FareRecommendRequest): FareRecommendResponse {
        // 1. on-demand feature 위도 경도 변환
        val orgH3: String = mapToH3(request.orgLat, request.orgLot);
        val dstH3: String = mapToH3(request.dstLat, request.dstLot);

        // 2. FareRecommendFeatureDTO를 통해 historical feature와 real-time feature 가져오기
        val feature: FareRecommendFeature = fareRecommendRepository.getFareRecommendFeature()

        // 3. FareRecommendFeatureDTO를 통해 FareRecommend 생성
        val fareRecommendDto: FareRecommendDto = getFareRecommendDto(request, orgH3, dstH3, feature)

        // 4. 모델서버로 전송
        return getRecommendFareFromModel(fareRecommendDto)
    }

    fun mapToH3(lat: Double, lot: Double): String {
        return h3Core.geoToH3Address(lat, lot, 7);
    }

    fun getRecommendFareFromModel(
        request: FareRecommendDto,
        url: String = "fare/recommend"
    ): FareRecommendResponse {
        val recommendFare: ResponseEntity<FareRecommendResponse> = restTemplate.postForEntity(
            url,
            request,
            FareRecommendResponse::class.java
        )
        recommendFare.body?.let {
            return it
        }
        throw IllegalStateException("모델의 반환값을 확인할 수 없습니다.")
    }

    // 이후 mapper로 대체예정
    fun getFareRecommendDto(
        request: FareRecommendRequest,
        orgH3: String,
        dstH3: String,
        feature: FareRecommendFeature
    ): FareRecommendDto {
        return FareRecommendDto(
            requestId = request.requestId,
            orgH3 = orgH3,
            dstH3 = dstH3,
            distance = request.distance,
            eta = request.eta,
            fareType = request.fareType,
            callCountNow = feature.realTimeFeature.callCountNow,
            dispatchCountNow = feature.realTimeFeature.dispatchCountNow,
            callCountAvgDuring2Week = feature.historicalFeature.callCountAvgDuring2Week,
            dispatchCountAvgDuring2Week = feature.historicalFeature.dispatchCountAvgDuring2Week
        )
    }
}
package com.example.spring_gateway.service

import com.example.spring_gateway.dto.FareRecommendDto
import com.example.spring_gateway.entity.feature.FareRecommendFeature
import com.example.spring_gateway.dto.request.FareRecommendRequest
import com.example.spring_gateway.dto.response.FareRecommendResponse
import com.example.spring_gateway.repository.FareRecommendRepository
import org.springframework.beans.factory.annotation.Autowired

class FareRecommendService(@Autowired val fareRecommendRepository: FareRecommendRepository) {

    fun getRecommendFare(request: FareRecommendRequest): FareRecommendResponse {
        // 1. on-demand feature 위도 경도 변환
        val orgH3: String = mapToH3(request.orgLat, request.orgLot);
        val dstH3: String = mapToH3(request.dstLat, request.dstLot);

        // 2. FareRecommendFeatureDTO를 통해 historical feature와 real-time feature 가져오기
        val feature: FareRecommendFeature = fareRecommendRepository.getFareRecommendFeature()

        // 3. FareRecommendFeatureDTO를 통해 FareRecommend 생성
        val fareRecommendDto: FareRecommendDto = getFareRecommendDto(request, feature)

        // 4. 모델서버로 전송
        val response: FareRecommendResponse = getRecommendFareFromModel(fareRecommendDto)

        return response
    }

    fun mapToH3(lat: Double, log: Double): String {
        TODO("on-demand feature 위도 경도 -> H3/res7로 변환")
    }

    fun getRecommendFareFromModel(
        request: FareRecommendDto,
        url: String = "model-server-url"
    ): FareRecommendResponse {
        TODO("FareRecommendDto를 모델 서버에 전달")
    }

    // 이후 mapper로 대체예정
    fun getFareRecommendDto(
        request: FareRecommendRequest,
        feature: FareRecommendFeature
    ): FareRecommendDto {
        TODO("fareRecommendDto 반환")
    }
}
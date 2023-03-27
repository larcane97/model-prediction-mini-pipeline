package com.example.spring_gateway.service

import com.example.spring_gateway.component.KeyGenerator
import com.example.spring_gateway.component.geoMapper.GeoMapper
import com.example.spring_gateway.dto.FareRecommendDto
import com.example.spring_gateway.entity.feature.FareRecommendFeature
import com.example.spring_gateway.dto.request.FareRecommendRequest
import com.example.spring_gateway.dto.response.FareRecommendResponse
import com.example.spring_gateway.entity.exception.IllegalMSResponseException
import com.example.spring_gateway.entity.exception.MSConnectionException
import com.example.spring_gateway.repository.FareRecommendRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity
import java.lang.AssertionError

@Service
class FareRecommendServiceImpl(
    @Autowired val fareRecommendRepository: FareRecommendRepository,
    @Autowired val restTemplate: RestTemplate,
    @Autowired val keyGenerator: KeyGenerator,
    @Autowired val geoMapper: GeoMapper
) : FareRecommendService {

    private val FARE_RECOMMEND_MODEL_SERVER_URL = "/fare/recommend"

    override fun getRecommendFare(request: FareRecommendRequest): FareRecommendResponse {
        // 1. on-demand feature 위도 경도 변환
        val orgGeoCode: String = geoMapper.geo2Address(request.orgLat, request.orgLot);
        val dstGeoCode: String = geoMapper.geo2Address(request.dstLat, request.dstLot);

        // 2. FareRecommendRequest를 통해 historical feature와 realtime feature 가져오기
        val historicalKey = keyGenerator.generateHistoricalKey(geoCode = orgGeoCode)
        val realtimeKey = keyGenerator.generateRealtimeKey(geoCode = dstGeoCode)
        val feature: FareRecommendFeature = getFareRecommendFeature(historicalKey, realtimeKey)

        // 3. FareRecommendFeatureDTO를 통해 FareRecommend 생성
        val fareRecommendDto: FareRecommendDto = getFareRecommendDto(request, orgGeoCode, dstGeoCode, feature)

        // 4. 모델서버로 전송
        return getRecommendFareFromModel(fareRecommendDto, FARE_RECOMMEND_MODEL_SERVER_URL)
    }

    override fun getFareRecommendFeature(
        historicalKey: List<String>, realtimeKey: String
    ): FareRecommendFeature {
        val historicalFeature = fareRecommendRepository.getHistoricalFeature(historicalKey)
        val realtimeFeature = fareRecommendRepository.getRealtimeFeature(realtimeKey)

        return FareRecommendFeature(
            historicalFeature = historicalFeature, realTimeFeature = realtimeFeature
        )
    }

    override fun getRecommendFareFromModel(
        request: FareRecommendDto, url: String
    ): FareRecommendResponse {
        return try {
            val recommendFare: ResponseEntity<FareRecommendResponse> = restTemplate.postForEntity(
                url, request, FareRecommendResponse::class
            )
            recommendFare.body ?: throw IllegalMSResponseException(request)
        } catch (e: Exception) {
            throw MSConnectionException(request)
        } catch (ex: AssertionError) {
            throw MSConnectionException(request)
        }
    }

    // 이후 mapper로 대체예정
    override fun getFareRecommendDto(
        request: FareRecommendRequest, orgH3: String, dstH3: String, feature: FareRecommendFeature
    ): FareRecommendDto {
        return FareRecommendDto(
            requestId = request.requestId,
            orgH3 = orgH3,
            dstH3 = dstH3,
            distance = request.distance,
            eta = request.eta,
            fareType = request.fareType,
            callCountNow = feature.realTimeFeature.callCountNow,
            callCountAvg = feature.historicalFeature.callCountAvg,
        )
    }
}
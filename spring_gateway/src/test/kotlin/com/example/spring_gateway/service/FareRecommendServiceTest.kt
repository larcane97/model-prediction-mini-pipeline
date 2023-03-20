package com.example.spring_gateway.service

import com.example.spring_gateway.config.RestTemplateConfig
import com.example.spring_gateway.dto.FareRecommendDto
import com.example.spring_gateway.dto.request.FareRecommendRequest
import com.example.spring_gateway.dto.response.FareRecommendResponse
import com.example.spring_gateway.entity.enums.FareType
import com.example.spring_gateway.entity.feature.FareRecommendFeature
import com.example.spring_gateway.entity.feature.HistoricalFeature
import com.example.spring_gateway.entity.feature.RealTimeFeature
import com.example.spring_gateway.repository.RedisFareRecommendRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.mockito.Mockito
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess

class FareRecommendServiceTest {
    private val fareRecommendRepository: RedisFareRecommendRepository =
        Mockito.mock(RedisFareRecommendRepository::class.java);
    private val objectMapper = jacksonObjectMapper();

    private var restTemplate = RestTemplateConfig.restTemplate();
    private var mockServer: MockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    private var fareRecommendService = FareRecommendService(fareRecommendRepository, restTemplate);

    @BeforeEach
    fun beforeEach(){
        restTemplate = RestTemplateConfig.restTemplate()
        mockServer = MockRestServiceServer.createServer(restTemplate);
        fareRecommendService = FareRecommendService(fareRecommendRepository, restTemplate);
    }
    @Test
    fun getRecommendFare() {
        // given
        Mockito.`when`(fareRecommendRepository.getRealTimeFeature())
            .thenReturn(RealTimeFeature(callCountNow = 100, dispatchCountNow = 200))
        Mockito.`when`(fareRecommendRepository.getHistoricalFeature())
            .thenReturn(HistoricalFeature(callCountAvgDuring2Week = 150, dispatchCountAvgDuring2Week = 180))
        Mockito.`when`(fareRecommendRepository.getFareRecommendFeature()).thenCallRealMethod();


        val response = FareRecommendResponse(1, 10000)
        mockServer.expect(requestTo("http://localhost:8000/fare/recommend"))
            .andRespond(withSuccess(objectMapper.writeValueAsString(response), MediaType.APPLICATION_JSON))

        val request = FareRecommendRequest(
            requestId = 1,
            orgLat = 37.394196,
            orgLot = 127.110191,
            dstLat = 37.394196,
            dstLot = 127.110191,
            distance = 1000,
            eta = 100,
            fareType = FareType.NORMAL.typeNumber)

        // when
        val recommendFare:FareRecommendResponse = fareRecommendService.getRecommendFare(request)

        // then
        assertThat(recommendFare).isNotNull;
    }

    @Test
    fun mapToH3() {
        // given
        val lat: Double = 37.394196;
        val lot: Double = 127.110191;

        // when
        val result = fareRecommendService.mapToH3(lat, lot)

        //then
        assertThat(result).isEqualTo("8730e1534ffffff")
    }

    @Test
    fun getRecommendFareFromModel() {
        // given
        val request = FareRecommendDto(
            requestId = 1,
            orgH3 = "8730e1534ffffff",
            dstH3 = "8730e1534ffffff",
            distance = 1000,
            eta = 10,
            callCountNow = 100,
            dispatchCountNow = 200,
            callCountAvgDuring2Week = 150,
            dispatchCountAvgDuring2Week = 180,
            fareType = FareType.NORMAL.typeNumber
        )

        val response = FareRecommendResponse(1, 10000)
        mockServer.expect(requestTo("http://localhost:8000/fare/recommend"))
            .andRespond(withSuccess(objectMapper.writeValueAsString(response), MediaType.APPLICATION_JSON))

        // when
        val recommendFareFromModel = fareRecommendService.getRecommendFareFromModel(request, url = "/fare/recommend")

        // then
        assertThat(recommendFareFromModel.requestId).isEqualTo(response.requestId)
    }

    @Test
    fun getFareRecommendDto() {
        // given
        val request = FareRecommendRequest(
            requestId = 1,
            orgLat = 0.0,
            orgLot = 0.0,
            dstLat = 0.0,
            dstLot = 0.0,
            distance = 1000,
            eta = 10,
            fareType = FareType.NORMAL.typeNumber
        )
        val feature = FareRecommendFeature(
            HistoricalFeature(200, 100), RealTimeFeature(20, 30)
        )

        // when
        val fareRecommendDto =
            fareRecommendService.getFareRecommendDto(request, "8730e1534ffffff", "8730e1534ffffff", feature);

        // then
        assertThat(fareRecommendDto).isNotNull;
    }
}
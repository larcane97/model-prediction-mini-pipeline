package com.example.spring_gateway.service

import com.example.spring_gateway.component.KeyGenerator
import com.example.spring_gateway.component.geoMapper.H3GeoMapper
import com.example.spring_gateway.config.RestTemplateConfig
import com.example.spring_gateway.dto.FareRecommendDto
import com.example.spring_gateway.dto.request.FareRecommendRequest
import com.example.spring_gateway.dto.response.FareRecommendResponse
import com.example.spring_gateway.entity.enums.FareType
import com.example.spring_gateway.entity.exception.FeatureReadException
import com.example.spring_gateway.entity.feature.FareRecommendFeature
import com.example.spring_gateway.entity.feature.HistoricalFeature
import com.example.spring_gateway.entity.feature.RealTimeFeature
import com.example.spring_gateway.repository.FareRecommendRepository
import com.example.spring_gateway.repository.RedisFareRecommendRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.RestTemplate

@SpringBootTest
class FareRecommendServiceImplTest {
    private val objectMapper = jacksonObjectMapper();

    private val opsForValueMock = Mockito.mock(ValueOperations::class.java) as ValueOperations<String, Any>
    private val redisTemplateMock = Mockito.mock(RedisTemplate::class.java)
    private val fareRecommendRepository: FareRecommendRepository =
        RedisFareRecommendRepository(redisTemplateMock as RedisTemplate<String, Any>)
    private val restTemplate: RestTemplate = RestTemplateConfig().restTemplate()
    private val keyGenerator = KeyGenerator()
    private val geoMapper = H3GeoMapper()
    private val fareRecommendService: FareRecommendServiceImpl =
        FareRecommendServiceImpl(fareRecommendRepository, restTemplate, keyGenerator, geoMapper)

    fun redisTemplateMocking() {
        Mockito.`when`(redisTemplateMock.opsForValue()).thenReturn(opsForValueMock)
        Mockito.`when`(redisTemplateMock.opsForValue().multiGet(Mockito.anyList())).thenAnswer {
            val keys = it.arguments[0] as List<String>
            if (keys == listOf("NO-KEY")) throw FeatureReadException("historical", key = "NO-KEY")
            else listOf(10, 10)

        }

        Mockito.`when`(redisTemplateMock.opsForValue().get(Mockito.anyString())).thenAnswer {
            val keys = it.arguments[0] as String
            if (keys == "NO-KEY") throw FeatureReadException("realtime", key = "NO-KEY")
            else 10
        }
    }

    @Test
    fun getRecommendFare() {
        // given
        val mockServer: MockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
        val mockUrl = restTemplate.uriTemplateHandler.expand("/").toString() + "fare/recommend"
        redisTemplateMocking()

        val request = FareRecommendRequest(
            requestId = 1,
            orgLat = 37.394196,
            orgLot = 127.110191,
            dstLat = 37.394196,
            dstLot = 127.110191,
            distance = 1000,
            eta = 100,
            fareType = FareType.NORMAL.typeNumber
        )

        val response = FareRecommendResponse(1, 10000)
        mockServer.expect(requestTo(mockUrl))
            .andRespond(withSuccess(objectMapper.writeValueAsString(response), MediaType.APPLICATION_JSON))

        // when
        val recommendFare: FareRecommendResponse = fareRecommendService.getRecommendFare(request)

        // then
        assertThat(recommendFare).isNotNull;
    }

    @Test
    fun getFareRecommendFeature() {
        redisTemplateMocking()

        // given
        val historicalKey2 = listOf("HISTORICAL_TEST")
        val realtimeKey2 = "REALTIME_TEST"
        // when
        val fareRecommendFeature2 = fareRecommendService.getFareRecommendFeature(historicalKey2, realtimeKey2)
        // then
        assertThat(fareRecommendFeature2.historicalFeature.callCountAvg).isEqualTo(10)
        assertThat(fareRecommendFeature2.realTimeFeature.callCountNow).isEqualTo(10)
    }

    @Test
    fun getRecommendFareFromModel() {
        val mockServer: MockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
        val mockUrl = restTemplate.uriTemplateHandler.expand("/").toString() + "fare/recommend"
        val response = FareRecommendResponse(1, 10000)
        mockServer.expect(requestTo(mockUrl))
            .andRespond(withSuccess(objectMapper.writeValueAsString(response), MediaType.APPLICATION_JSON))

        // case1. 정상
        // given
        val featureDto1 = FareRecommendDto(1, "TEST", "TEST", 1000, 100, 10, 10, FareType.NORMAL.typeNumber)
        //when
        val fareRecommend1 = fareRecommendService.getRecommendFareFromModel(featureDto1, "/fare/recommend")
        // then
        assertThat(fareRecommend1.requestId).isEqualTo(1)
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
            HistoricalFeature(200), RealTimeFeature(20)
        )

        // when
        val fareRecommendDto =
            fareRecommendService.getFareRecommendDto(request, "8730e1534ffffff", "8730e1534ffffff", feature);

        // then
        assertThat(fareRecommendDto).isNotNull;
    }
}
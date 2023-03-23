package com.example.spring_gateway.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class RedisFareRecommendRepositoryTest {
    private val fareRecommendRepository = RedisFareRecommendRepository();

    @Test
    fun getHistoricalFeature() {
        // given

        // when
        val historicalFeature = fareRecommendRepository.getHistoricalFeature()

        // then
        assertThat(historicalFeature.callCountAvgDuring2Week).isGreaterThan(0)
        assertThat(historicalFeature.dispatchCountAvgDuring2Week).isGreaterThan(0)
    }

    @Test
    fun getRealTimeFeature() {
        // given

        // when
        val realTimeFeature = fareRecommendRepository.getRealTimeFeature()

        // then
        assertThat(realTimeFeature.callCountNow).isGreaterThan(0)
        assertThat(realTimeFeature.dispatchCountNow).isGreaterThan(0)
    }
}
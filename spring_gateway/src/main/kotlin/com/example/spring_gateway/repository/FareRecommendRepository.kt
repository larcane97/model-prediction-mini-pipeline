package com.example.spring_gateway.repository

import com.example.spring_gateway.entity.feature.FareRecommendFeature
import com.example.spring_gateway.entity.feature.HistoricalFeature
import com.example.spring_gateway.entity.feature.RealTimeFeature

interface FareRecommendRepository {
    fun save(fareRecommendFeature: FareRecommendFeature);
    fun getHistoricalFeature(): HistoricalFeature;
    fun getRealTimeFeature(): RealTimeFeature;
    fun getFareRecommendFeature(): FareRecommendFeature {
        val historicalFeature : HistoricalFeature = this.getHistoricalFeature();
        val realTimeFeature : RealTimeFeature = this.getRealTimeFeature()
        return FareRecommendFeature(
            historicalFeature = historicalFeature,
            realTimeFeature = realTimeFeature);
    };
}
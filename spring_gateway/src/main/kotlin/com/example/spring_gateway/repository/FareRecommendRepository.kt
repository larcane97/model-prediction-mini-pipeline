package com.example.spring_gateway.repository

import com.example.spring_gateway.entity.error.FeatureReadException
import com.example.spring_gateway.entity.feature.FareRecommendFeature
import com.example.spring_gateway.entity.feature.HistoricalFeature
import com.example.spring_gateway.entity.feature.RealTimeFeature

interface FareRecommendRepository {
    fun save(fareRecommendFeature: FareRecommendFeature);
    fun getHistoricalFeature(): HistoricalFeature;
    fun getRealTimeFeature(): RealTimeFeature;
    fun getFareRecommendFeature(): FareRecommendFeature {
        var historicalFeature:HistoricalFeature;
        val realTimeFeature:RealTimeFeature
        try {
            historicalFeature = this.getHistoricalFeature();
            realTimeFeature = this.getRealTimeFeature()
        }
        catch(e:Exception){
            throw FeatureReadException()
        }

        return FareRecommendFeature(
            historicalFeature = historicalFeature,
            realTimeFeature = realTimeFeature);
    };
}
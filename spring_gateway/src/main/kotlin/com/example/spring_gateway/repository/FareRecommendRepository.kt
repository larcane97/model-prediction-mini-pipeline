package com.example.spring_gateway.repository

import com.example.spring_gateway.entity.feature.FareRecommendFeature
import com.example.spring_gateway.entity.feature.HistoricalFeature
import com.example.spring_gateway.entity.feature.RealTimeFeature

interface FareRecommendRepository {
    fun save(fareRecommendFeature: FareRecommendFeature);
    fun getHistoricalFeature(key: List<String>): HistoricalFeature;
    fun getRealtimeFeature(key: String): RealTimeFeature;
}
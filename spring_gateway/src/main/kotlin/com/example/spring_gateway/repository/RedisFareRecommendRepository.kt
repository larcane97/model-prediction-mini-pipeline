package com.example.spring_gateway.repository

import com.example.spring_gateway.entity.feature.FareRecommendFeature
import com.example.spring_gateway.entity.feature.HistoricalFeature
import com.example.spring_gateway.entity.feature.RealTimeFeature
import java.lang.IllegalStateException

class RedisFareRecommendRepository:FareRecommendRepository {
    override fun save(fareRecommendFeature: FareRecommendFeature) {
        throw IllegalStateException("RedisRepository에서는 save를 할 수 없습니다.");
    }

    override fun getHistoricalFeature(): HistoricalFeature {
        TODO("redis에서부터 historical feature 읽어오기")
    }

    override fun getRealTimeFeature(): RealTimeFeature {
        TODO("redis에서부터 real-time feature 읽어오기")
    }

}
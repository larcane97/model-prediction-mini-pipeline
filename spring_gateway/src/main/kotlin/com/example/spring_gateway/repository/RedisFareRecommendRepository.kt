package com.example.spring_gateway.repository

import com.example.spring_gateway.entity.feature.FareRecommendFeature
import com.example.spring_gateway.entity.feature.HistoricalFeature
import com.example.spring_gateway.entity.feature.RealTimeFeature
import org.springframework.stereotype.Repository
import java.lang.IllegalStateException

@Repository
class RedisFareRecommendRepository:FareRecommendRepository {
    override fun save(fareRecommendFeature: FareRecommendFeature) {
        throw IllegalStateException("RedisRepository에서는 save를 할 수 없습니다.");
    }

    // 수정 예정 : redis로부터 값 읽어오기
    override fun getHistoricalFeature(): HistoricalFeature {
        return HistoricalFeature(callCountAvgDuring2Week = 500, dispatchCountAvgDuring2Week = 350)
    }

    // 수정 예정 : redis로부터 값 읽어오기
    override fun getRealTimeFeature(): RealTimeFeature {
        return RealTimeFeature(callCountNow = 800, dispatchCountNow = 600)
    }

}
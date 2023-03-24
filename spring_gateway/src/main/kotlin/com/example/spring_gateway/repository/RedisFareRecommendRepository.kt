package com.example.spring_gateway.repository

import com.example.spring_gateway.entity.exception.FeatureReadException
import com.example.spring_gateway.entity.feature.FareRecommendFeature
import com.example.spring_gateway.entity.feature.HistoricalFeature
import com.example.spring_gateway.entity.feature.RealTimeFeature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.lang.IllegalStateException

@Repository
class RedisFareRecommendRepository(
    @Autowired val redisTemplate: RedisTemplate<String, Any>
) : FareRecommendRepository {
    override fun save(fareRecommendFeature: FareRecommendFeature) {
        throw IllegalStateException("RedisRepository에서는 save를 할 수 없습니다.");
    }

    override fun getHistoricalFeature(key: List<String>): HistoricalFeature {
        val values = redisTemplate.opsForValue().multiGet(key)
        if(values.isNullOrEmpty()){
            throw IllegalStateException()
        }
        else {
            val callCountAvg = values.map{it.toString().toLong()}.average().toLong()
            return HistoricalFeature(callCountAvg = callCountAvg)
        }
    }

    override fun getRealtimeFeature(key: String): RealTimeFeature {
        val callCountNow = redisTemplate.opsForValue().get(key).toString().toLong()

        return RealTimeFeature(callCountNow = callCountNow?.toLong())
    }

}
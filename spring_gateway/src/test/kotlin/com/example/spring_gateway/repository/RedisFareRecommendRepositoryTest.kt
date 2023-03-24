package com.example.spring_gateway.repository

import com.example.spring_gateway.component.KeyGenerator
import com.example.spring_gateway.entity.exception.FeatureReadException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.fail

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.connection.RedisConnectionFactory
import java.time.LocalDateTime
import java.time.ZoneId

@SpringBootTest
class RedisFareRecommendRepositoryTest(
    @Autowired val fareRecommendRepository: RedisFareRecommendRepository,
    @Autowired val keyGenerator: KeyGenerator,
    @Autowired val connectionFactory : RedisConnectionFactory
) {

    @Test
    fun redisConnectionTest() {
        try {
            val connection = connectionFactory.connection
            connection.close()
            println("Connected to Redis server successfully")
        } catch (e: Exception) {
            println("Failed to connect to Redis server: ${e.message}")
            fail("failed to connect to redis server")
        }
    }
    @Test
    fun getHistoricalFeature() {
        // given
        val historicalKey1 = keyGenerator.generateHistoricalKey("8730d3a42ffffff",LocalDateTime.now(ZoneId.of("Asia/Seoul")))
        // when
        val historicalFeature1 = fareRecommendRepository.getHistoricalFeature(historicalKey1)
        // then
        assertThat(historicalFeature1.callCountAvg).isGreaterThan(0)
        println("historical feature of $historicalKey1 : $historicalFeature1")

        // given
        val historicalKey2 = keyGenerator.generateHistoricalKey("NO-KEY",LocalDateTime.now(ZoneId.of("Asia/Seoul")))
        // when
        val exception =
            assertThrows<FeatureReadException> { fareRecommendRepository.getHistoricalFeature(historicalKey2) }
        // then
        assertThat(exception.featureType).isEqualTo("historical")

    }

    @Test
    fun getRealTimeFeature() {
        // given
        val realtimeKey1 = keyGenerator.generateRealtimeKey("8730d3a42ffffff",LocalDateTime.now(ZoneId.of("Asia/Seoul")))
        // when
        val realtimeFeature1 = fareRecommendRepository.getRealtimeFeature(realtimeKey1)
        // then
        assertThat(realtimeFeature1.callCountNow).isGreaterThan(0)
        println("realtime feature of $realtimeKey1 : $realtimeFeature1")

        // given
        val realtimeKey2 = keyGenerator.generateRealtimeKey("NO-KEY",LocalDateTime.now(ZoneId.of("Asia/Seoul")))
        // when
        val exception = assertThrows<FeatureReadException> {
            fareRecommendRepository.getRealtimeFeature(realtimeKey2)
        }
        // then
        assertThat(exception.featureType).isEqualTo("realtime")
        assertThat(exception.key.take(6)).isEqualTo("NO-KEY")
    }
}
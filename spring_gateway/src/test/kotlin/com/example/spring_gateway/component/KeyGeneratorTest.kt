package com.example.spring_gateway.component

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import java.time.LocalDateTime

class KeyGeneratorTest {
    private val keyGenerator = KeyGenerator()
    private val testLocalDateTime = LocalDateTime.of(2023, 3, 24, 0, 42)
    private val testHcode = "TEST"

    @Test
    fun generateHistoricalKey() {
        // given
        val formatter = keyGenerator.historicalFormatter
        val expectedHistoricalKeyList = listOf("TEST23031700", "TEST23031000")

        // when
        val historicalKeyList = keyGenerator.generateHistoricalKey(testHcode, testLocalDateTime)

        // then
        assertThat(historicalKeyList).isEqualTo(expectedHistoricalKeyList)

    }

    @Test
    fun generateRealtimeKey() {
        // given
        val formatter = keyGenerator.realtimeFormatter
        val expectedRealtimeKey = "TEST2303240040"

        // when
        val realtimeKey = keyGenerator.generateRealtimeKey(testHcode, testLocalDateTime)

        // then
        assertThat(realtimeKey).isEqualTo(expectedRealtimeKey)

    }
}
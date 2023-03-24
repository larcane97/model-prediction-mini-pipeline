package com.example.spring_gateway.component

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Component
class KeyGenerator {
    @Value("\${key.historical.historicalWindow}")
    val historicalWindow: Int = 2
    val zoneId: String = "Asia/Seoul"
    val historicalFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyMMddHH")
    val realtimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyMMddHHmm")

    fun generateHistoricalKey(hcode: String, time: LocalDateTime? = null): List<String> {
        val time = time ?: LocalDateTime.now(ZoneId.of(zoneId))
        val keyList = ArrayList<String>()
        for (i in 1..historicalWindow) {
            val timeKey = time.minusWeeks(i.toLong()).format(historicalFormatter)
            keyList.add(hcode + timeKey)
        }
        return keyList
    }

    fun generateRealtimeKey(hcode: String, time: LocalDateTime? = null): String {
        val time = time ?: LocalDateTime.now(ZoneId.of(zoneId))
        val timeKey: String = time.truncatedTo(ChronoUnit.HOURS)
            .plusMinutes((time.minute / 10L) * 10)
            .format(realtimeFormatter)
        return hcode + timeKey
    }

    fun getPrevHistoricalKey(key: String): List<String> {
        val hcode = key.substring(0,key.length - 8)
        val prevTime = LocalDateTime.parse(key.takeLast(8), historicalFormatter).minusHours(1)
        return generateHistoricalKey(hcode, prevTime)
    }

    fun getPrevRealtimeKey(key: String): String {
        val hcode = key.substring(0,key.length-10)
        val prevTime = LocalDateTime.parse(key.takeLast(10), realtimeFormatter).minusMinutes(10)
        return generateRealtimeKey(hcode, prevTime)
    }
}
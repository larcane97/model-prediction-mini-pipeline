package com.example.spring_gateway

import com.example.spring_gateway.dto.request.FareRecommendRequest
import com.example.spring_gateway.dto.response.FareRecommendResponse
import com.example.spring_gateway.entity.enums.FareType
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class SpringGatewayApplicationTests(
    @Autowired val mockMvc: MockMvc
) {
    private val objectMapper = jacksonObjectMapper();

    @Test
    fun contextLoads() {
    }

    @Test
    fun getRecommendFare() {
        // given
        val request = FareRecommendRequest(
            requestId = 1,
            orgLat = 37.394196,
            orgLot = 127.110191,
            dstLat = 37.394196,
            dstLot = 127.110191,
            distance = 1000,
            eta = 100,
            fareType = FareType.NORMAL.typeNumber
        )
        val uri: String = "/fare/recommend"

        // when
        val response = mockMvc.perform(
            MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)).accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk).andReturn().response.contentAsString

        val recommmendFare: FareRecommendResponse = objectMapper.readValue(response)

        // then
        assertThat(recommmendFare.requestId).isEqualTo(1)
    }

}

package com.example.spring_gateway

import com.example.spring_gateway.dto.request.FareRecommendRequest
import com.example.spring_gateway.dto.response.FareRecommendResponse
import com.example.spring_gateway.entity.enums.FareType
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers
import org.springframework.test.web.client.response.MockRestResponseCreators
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.web.client.RestTemplate

@SpringBootTest
@AutoConfigureMockMvc
class SpringGatewayApplicationTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val restTemplate:RestTemplate
) {
    private val objectMapper = jacksonObjectMapper();
    private var mockServer: MockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    @Value("\${restTemplate.baseUrl}")
    private val fareRecommendBaseUrl: String = ""

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

        val expectedResponse = FareRecommendResponse(1, 10000)
        mockServer.expect(MockRestRequestMatchers.requestTo(fareRecommendBaseUrl+uri))
            .andRespond(
                MockRestResponseCreators.withSuccess(
                    objectMapper.writeValueAsString(expectedResponse),
                    MediaType.APPLICATION_JSON
                )
            )


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

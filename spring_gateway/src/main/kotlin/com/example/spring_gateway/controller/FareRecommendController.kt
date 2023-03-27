package com.example.spring_gateway.controller

import com.example.spring_gateway.dto.request.FareRecommendRequest
import com.example.spring_gateway.dto.response.FareRecommendResponse
import com.example.spring_gateway.service.FareRecommendServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class FareRecommendController(
    @Autowired val fareRecommendServiceImpl: FareRecommendServiceImpl
) {

    @RequestMapping(value=["/fare/recommend"], method = [RequestMethod.POST])
    fun getRecommendedFare(
        @RequestBody request: FareRecommendRequest
    ): ResponseEntity<FareRecommendResponse> {
        return ResponseEntity.ok().body(
            fareRecommendServiceImpl.getRecommendFare(request)
        )
    }
}
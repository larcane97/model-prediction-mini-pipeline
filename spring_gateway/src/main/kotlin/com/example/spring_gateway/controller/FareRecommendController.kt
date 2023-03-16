package com.example.spring_gateway.controller

import com.example.spring_gateway.dto.request.FareRecommendRequest
import com.example.spring_gateway.dto.response.FareRecommendResponse
import com.example.spring_gateway.service.FareRecommendService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class FareRecommendController(
    @Autowired val fareRecommendService: FareRecommendService
) {

    @PostMapping("/fare/recommend")
    fun getRecommendedFare(
        @RequestBody request: FareRecommendRequest
    ): ResponseEntity<FareRecommendResponse> {
        return ResponseEntity.ok().body(
            fareRecommendService.getRecommendFare(request)
        )
    }
}
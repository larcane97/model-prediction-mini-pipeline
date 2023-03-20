from src.dto.request.FareRecommendRequest import FareRecommendRequest
from src.entity.RecommendFare import RecommendFare


class SimpleModel:
    def predict(self, request: FareRecommendRequest) -> RecommendFare:
        return RecommendFare(
            requestId=request.requestId,
            recommendFare=10000)

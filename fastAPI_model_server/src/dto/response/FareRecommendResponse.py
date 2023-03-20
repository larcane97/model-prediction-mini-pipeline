from pydantic import BaseModel


class FareRecommendResponse(BaseModel):
    requestId: int
    recommendFare: int

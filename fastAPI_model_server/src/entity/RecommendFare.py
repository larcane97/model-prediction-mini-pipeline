from pydantic import BaseModel


class RecommendFare(BaseModel):
    requestId: int
    recommendFare: int

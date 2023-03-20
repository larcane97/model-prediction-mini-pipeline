from src.dto.request.FareRecommendRequest import FareRecommendRequest
from src.dto.response.FareRecommendResponse import FareRecommendResponse
from src.entity.RecommendFare import RecommendFare
from src.model.simpleModel import SimpleModel

from fastapi import FastAPI

app = FastAPI()
model = SimpleModel()


@app.post("/fare/recommend",)
def predict_fare_recommend(request: FareRecommendRequest) -> FareRecommendResponse:
    recommend_fare: RecommendFare = model.predict(request)
    recommend_fare_response = FareRecommendResponse(**recommend_fare.dict())
    return recommend_fare_response

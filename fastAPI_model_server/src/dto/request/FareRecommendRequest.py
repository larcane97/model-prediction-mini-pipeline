from pydantic import BaseModel


class FareRecommendRequest(BaseModel):
    requestId: int
    orgH3: str
    dstH3: str
    distance: int
    eta: int
    callCountNow: int
    callCountAvg: int
    fareType: int

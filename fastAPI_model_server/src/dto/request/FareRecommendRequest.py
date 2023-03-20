from pydantic import BaseModel


class FareRecommendRequest(BaseModel):
    requestId: int
    orgH3: str
    dstH3: str
    distance: int
    eta: int
    callCountNow: int
    dispatchCountNow: int
    callCountAvgDuring2Week: int
    dispatchCountAvgDuring2Week: int
    fareType: int

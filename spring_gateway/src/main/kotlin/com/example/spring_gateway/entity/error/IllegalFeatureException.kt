package com.example.spring_gateway.entity.error

class IllegalFeatureException(message:String="feature를 가져올 수 없습니다."):Exception(message)
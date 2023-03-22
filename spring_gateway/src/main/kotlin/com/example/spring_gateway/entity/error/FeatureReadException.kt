package com.example.spring_gateway.entity.error

class FeatureReadException(message:String="Feature를 가져올 수 없습니다."):Exception(message)
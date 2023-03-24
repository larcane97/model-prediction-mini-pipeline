package com.example.spring_gateway.entity.exception

class FeatureReadException(val featureType: String, val key: String):Exception()
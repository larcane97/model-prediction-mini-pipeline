package com.example.spring_gateway.component.geoMapper

interface GeoMapper {
    fun geo2Address(lat: Double, lng: Double): String

    fun address2Geo(code: String): List<Double>
}
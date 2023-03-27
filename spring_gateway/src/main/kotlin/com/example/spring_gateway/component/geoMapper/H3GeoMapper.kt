package com.example.spring_gateway.component.geoMapper

import com.uber.h3core.H3Core
import org.springframework.stereotype.Component

@Component
class H3GeoMapper(private val res: Int = 7) : GeoMapper {
    private val h3Core = H3Core.newInstance()
    override fun geo2Address(lat: Double, lng: Double): String {
        return h3Core.geoToH3Address(lat, lng, res)
    }

    override fun address2Geo(code: String): List<Double> {
        val geo = h3Core.h3ToGeo(code)
        return listOf(geo.lat, geo.lng)
    }

}
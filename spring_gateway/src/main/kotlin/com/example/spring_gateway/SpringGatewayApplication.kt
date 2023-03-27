package com.example.spring_gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
class SpringGatewayApplication

fun main(args: Array<String>) {
	runApplication<SpringGatewayApplication>(*args)
}

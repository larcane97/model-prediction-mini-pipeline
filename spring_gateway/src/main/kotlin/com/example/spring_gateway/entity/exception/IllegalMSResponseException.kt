package com.example.spring_gateway.entity.exception

class IllegalMSResponseException(message:String="모델 서버의 결과가 잘못 되었습니다."):Exception(message) {
}
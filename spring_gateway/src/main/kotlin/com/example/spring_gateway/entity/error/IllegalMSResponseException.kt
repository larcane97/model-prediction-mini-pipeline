package com.example.spring_gateway.entity.error

class IllegalMSResponseException(message:String="모델 서버의 결과가 잘못 되었습니다."):Exception(message) {
}
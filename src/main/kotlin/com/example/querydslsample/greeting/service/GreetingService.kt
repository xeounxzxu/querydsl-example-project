package com.example.querydslsample.greeting.service

import com.example.querydslsample.greeting.dto.GreetingResponse
import org.springframework.stereotype.Service

@Service
class GreetingService {
    fun greet(name: String): GreetingResponse = GreetingResponse("Hello, ${name.trim()}!")
}

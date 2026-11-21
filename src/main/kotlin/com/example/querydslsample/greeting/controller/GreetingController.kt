package com.example.querydslsample.greeting.controller

import com.example.querydslsample.greeting.dto.GreetingResponse
import com.example.querydslsample.greeting.service.GreetingService
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/greetings")
class GreetingController(private val greetingService: GreetingService) {
    @GetMapping
    fun greet(
        @RequestParam(defaultValue = "World")
        @NotBlank
        @Size(max = 100)
        name: String,
    ): GreetingResponse = greetingService.greet(name)
}

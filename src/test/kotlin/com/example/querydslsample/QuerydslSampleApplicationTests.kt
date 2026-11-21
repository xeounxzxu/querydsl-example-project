package com.example.querydslsample

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class QuerydslSampleApplicationTests @Autowired constructor(private val mockMvc: MockMvc) {
    @Test
    fun `default greeting is returned as JSON`() {
        mockMvc.get("/api/v1/greetings").andExpect {
            status { isOk() }
            jsonPath("$.message") { value("Hello, World!") }
        }
    }

    @Test
    fun `custom name is trimmed`() {
        mockMvc.get("/api/v1/greetings") { param("name", " Kotlin ") }.andExpect {
            status { isOk() }
            jsonPath("$.message") { value("Hello, Kotlin!") }
        }
    }

    @Test
    fun `blank and oversized names are rejected`() {
        listOf("   ", "a".repeat(101)).forEach { name ->
            mockMvc.get("/api/v1/greetings") { param("name", name) }.andExpect {
                status { isBadRequest() }
            }
        }
    }

    @Test
    fun `health endpoint is available`() {
        mockMvc.get("/actuator/health").andExpect {
            status { isOk() }
            jsonPath("$.status") { value("UP") }
        }
    }
}

package com.example.querydslsample.member

import com.example.querydslsample.member.entity.Member
import com.example.querydslsample.member.repository.MemberRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberApiTests @Autowired constructor(
    private val mockMvc: MockMvc,
    private val memberRepository: MemberRepository,
) {
    @Test
    fun `created member can be searched using Querydsl`() {
        mockMvc.post("/api/v1/members") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"name":" Alice ","age":30}"""
        }.andExpect {
            status { isCreated() }
            jsonPath("$.id") { isNumber() }
            jsonPath("$.name") { value("Alice") }
        }
        mockMvc.get("/api/v1/members") { param("name", "alice"); param("minAge", "30") }.andExpect {
            status { isOk() }
            jsonPath("$[0].name") { value("Alice") }
            jsonPath("$.length()") { value(1) }
        }
    }

    @Test
    fun `optional filters combine and results are limited in id order`() {
        memberRepository.saveAll(listOf(Member("Alice", 20), Member("ALICIA", 30), Member("Bob", 40)))
        mockMvc.get("/api/v1/members") { param("name", "ali"); param("minAge", "25") }.andExpect {
            status { isOk() }
            jsonPath("$.length()") { value(1) }
            jsonPath("$[0].name") { value("ALICIA") }
        }
        mockMvc.get("/api/v1/members") { param("limit", "2") }.andExpect {
            status { isOk() }
            jsonPath("$.length()") { value(2) }
            jsonPath("$[0].name") { value("Alice") }
        }
        mockMvc.get("/api/v1/members") { param("name", "missing") }.andExpect {
            status { isOk() }
            jsonPath("$.length()") { value(0) }
        }
    }

    @Test
    fun `invalid member and query parameters are rejected`() {
        mockMvc.post("/api/v1/members") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"name":" ","age":-1}"""
        }.andExpect { status { isBadRequest() } }
        listOf("minAge" to "-1", "limit" to "0", "limit" to "101").forEach { (key, value) ->
            mockMvc.get("/api/v1/members") { param(key, value) }.andExpect { status { isBadRequest() } }
        }
    }
}

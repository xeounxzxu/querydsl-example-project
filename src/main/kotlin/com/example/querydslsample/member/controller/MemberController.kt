package com.example.querydslsample.member.controller

import com.example.querydslsample.member.dto.CreateMemberRequest
import com.example.querydslsample.member.dto.MemberResponse
import com.example.querydslsample.member.service.MemberService
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/members")
class MemberController(private val memberService: MemberService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CreateMemberRequest): MemberResponse = memberService.create(request)

    @GetMapping
    fun search(
        @RequestParam(required = false) @Size(max = 100) name: String?,
        @RequestParam(required = false) @Min(0) @Max(150) minAge: Int?,
        @RequestParam(defaultValue = "20") @Min(1) @Max(100) limit: Int,
    ): List<MemberResponse> = memberService.search(name, minAge, limit)
}

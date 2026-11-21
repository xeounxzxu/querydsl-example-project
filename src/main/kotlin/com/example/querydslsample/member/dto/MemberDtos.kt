package com.example.querydslsample.member.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateMemberRequest(
    @field:NotBlank @field:Size(max = 100) val name: String,
    @field:Min(0) @field:Max(150) val age: Int,
)

data class MemberResponse(val id: Long, val name: String, val age: Int)

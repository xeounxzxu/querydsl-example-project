package com.example.querydslsample.member.service

import com.example.querydslsample.member.dto.CreateMemberRequest
import com.example.querydslsample.member.dto.MemberResponse
import com.example.querydslsample.member.entity.Member
import com.example.querydslsample.member.repository.MemberQueryRepository
import com.example.querydslsample.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberQueryRepository: MemberQueryRepository,
) {
    @Transactional
    fun create(request: CreateMemberRequest): MemberResponse =
        memberRepository.save(Member(name = request.name.trim(), age = request.age)).toResponse()

    fun search(name: String?, minAge: Int?, limit: Int): List<MemberResponse> =
        memberQueryRepository.search(name, minAge, limit).map { it.toResponse() }

    private fun Member.toResponse() = MemberResponse(requireNotNull(id), name, age)
}

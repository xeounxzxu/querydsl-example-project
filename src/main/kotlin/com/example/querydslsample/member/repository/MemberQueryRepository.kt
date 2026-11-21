package com.example.querydslsample.member.repository

import com.example.querydslsample.member.entity.Member
import com.example.querydslsample.member.entity.QMember.member
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class MemberQueryRepository(private val queryFactory: JPAQueryFactory) {
    fun search(name: String?, minAge: Int?, limit: Int): List<Member> = queryFactory
        .selectFrom(member)
        .where(
            name?.trim()?.takeIf { it.isNotEmpty() }?.let { member.name.containsIgnoreCase(it) },
            minAge?.let { member.age.goe(it) },
        )
        .orderBy(member.id.asc())
        .limit(limit.toLong())
        .fetch()
}

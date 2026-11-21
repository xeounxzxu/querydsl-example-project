package com.example.querydslsample.member.repository

import com.example.querydslsample.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>

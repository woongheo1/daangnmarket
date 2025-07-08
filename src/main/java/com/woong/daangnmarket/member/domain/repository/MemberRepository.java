package com.woong.daangnmarket.member.domain.repository;

import com.woong.daangnmarket.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
}

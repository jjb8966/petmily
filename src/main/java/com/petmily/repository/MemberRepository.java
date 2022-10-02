package com.petmily.repository;

import com.petmily.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Long save(Member member);

    Optional<Member> findById(Long id);

    List<Member> findAll();

    Optional<Member> findByName(String name);
}

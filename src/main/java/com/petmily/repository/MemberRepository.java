package com.petmily.repository;

import com.petmily.domain.Member;

public interface MemberRepository {

    Long save(Member member);

    Member find(Long id);
}

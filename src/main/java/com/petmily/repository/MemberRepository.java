package com.petmily.repository;

import com.petmily.domain.Member;

import java.util.List;

public interface MemberRepository {

    Long save(Member member);

    Member find(Long id);
}

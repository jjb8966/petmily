package com.petmily.repository;

import com.petmily.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long save(Member member) {
        em.persist(member);

        return member.getId();
    }

    @Override
    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}

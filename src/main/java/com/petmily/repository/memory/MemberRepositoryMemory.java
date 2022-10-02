package com.petmily.repository.memory;

import com.petmily.domain.Member;
import com.petmily.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
//@Qualifier("memory")
public class MemberRepositoryMemory implements MemberRepository {

    private static Map<Long, Member> store = new ConcurrentHashMap<>();
    private static Long sequence = 0L;

    @Override
    public Long save(Member member) {
        member.setId(sequence++);
        store.put(member.getId(), member);

        return member.getId();
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = store.get(id);

        return Optional.ofNullable(member);
    }

    @Override
    public List<Member> findAll() {
        Collection<Member> values = store.values();

        return new ArrayList<>(values);
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }
}

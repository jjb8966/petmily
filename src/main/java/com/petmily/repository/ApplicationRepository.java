package com.petmily.repository;

import com.petmily.domain.core.Member;
import com.petmily.domain.core.application.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findAllByMemberOrderByApplicationType(Member member);

    List<Application> findAllByApplicationType(String applicationType);
}

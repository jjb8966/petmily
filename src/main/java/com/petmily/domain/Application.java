package com.petmily.domain;

import com.petmily.domain.enum_type.ApplicationStatus;
import com.petmily.domain.enum_type.ApplicationType;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
public class Application {

    @Id @GeneratedValue
    @Column(name = "application_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "abandonedAnimal_id")
    private AbandonedAnimal abandonedAnimal;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @Enumerated(EnumType.STRING)
    private ApplicationType applicationType;
}

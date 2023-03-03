package com.petmily.domain.core.application;

import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.core.BaseEntity;
import com.petmily.domain.core.Member;
import com.petmily.domain.enum_type.ApplicationStatus;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "app_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Application extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "application_id")
    private Long id;

    @Column(name = "app_type", insertable = false, updatable = false)
    private String applicationType;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    protected Member member;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "abandonedAnimal_id")
    protected AbandonedAnimal abandonedAnimal;

    @Enumerated(EnumType.STRING)
    protected ApplicationStatus applicationStatus;

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", member=" + member.getId() +
                ", abandonedAnimal=" + abandonedAnimal.getId() +
                ", applicationStatus=" + applicationStatus +
                '}';
    }

    public void approve() {
        this.applicationStatus = ApplicationStatus.APPROVE;
    }

    public void refuse() {
        this.applicationStatus = ApplicationStatus.REFUSE;
    }
}

package com.petmily.domain.core.embeded_type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {

    private String id;
    private String site;
    private String domain;

    public Email(String id, String site, String domain) {
        this.id = id;
        this.site = site;
        this.domain = domain;
    }

    public String fullName() {
        return id + "@" + site + "." + domain;
    }
}

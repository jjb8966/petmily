package com.petmily.domain.embedded_type;

import javax.persistence.Embeddable;

@Embeddable
public class Picture {

    private String imagePath;
    private String fileName;
}

package com.petmily;

import com.petmily.domain.builder.AbandonedAnimalBuilder;
import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.builder.PictureBuilder;
import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.Picture;
import com.petmily.domain.core.enum_type.AnimalSpecies;
import com.petmily.service.AbandonedAnimalService;
import com.petmily.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class InitService {

    private final MemberService memberService;
    private final AbandonedAnimalService abandonedAnimalService;

    @PostConstruct
    public void init() {
        initMember();
        initAbandonedAnimal();
    }

    private void initMember() {
        Member admin = new MemberBuilder("ADMIN", "123")
                .setName("관리자")
                .build();

        memberService.join(admin);
    }

    private void initAbandonedAnimal() {
        for (int i = 1; i <= 50; i++) {
            AbandonedAnimal animal = new AbandonedAnimalBuilder()
                    .setName("멍멍이" + i)
                    .setAge(i)
                    .setSpecies(AnimalSpecies.DOG)
                    .setKind("진돗개")
                    .setWeight(i)
                    .build();

            String imageName = "dog" + (i % 5 + 1) + ".jpeg";
            Picture picture = new PictureBuilder(animal, imageName).build();

            abandonedAnimalService.register(animal);
        }
    }
}

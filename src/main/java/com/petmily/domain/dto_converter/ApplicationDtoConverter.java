package com.petmily.domain.dto_converter;

import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.core.BaseEntity;
import com.petmily.domain.core.application.Adopt;
import com.petmily.domain.core.application.Application;
import com.petmily.domain.core.application.Donation;
import com.petmily.domain.core.application.TemporaryProtection;
import com.petmily.domain.dto.application.AdoptDetailForm;
import com.petmily.domain.dto.application.ApplicationListForm;
import com.petmily.domain.dto.application.DonationDetailForm;
import com.petmily.domain.dto.application.TempProtectionDetailForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class ApplicationDtoConverter implements EntityDtoConverter {

    @Override
    public <T> Optional<T> entityToDto(BaseEntity entity, Class<T> dtoType) {
        Application application = (Application) entity;
        T dto = null;

        if (ApplicationListForm.class.isAssignableFrom(dtoType)) {
            dto = (T) convertApplicationListForm(application);
        }

        if (DonationDetailForm.class.isAssignableFrom(dtoType)) {
            dto = (T) convertToDonationDetailForm(application);
        }

        if (TempProtectionDetailForm.class.isAssignableFrom(dtoType)) {
            dto = (T) convertToTempProtectionDetailForm(application);
        }

        if (AdoptDetailForm.class.isAssignableFrom(dtoType)) {
            dto = (T) convertToAdoptDetailForm(application);
        }

        return Optional.ofNullable(dto);
    }

    private ApplicationListForm convertApplicationListForm(Application application) {
        ApplicationListForm form = new ApplicationListForm();

        form.setId(application.getId());
        form.setAnimalName(application.getAbandonedAnimal().getName());
        form.setType(application.getApplicationType());
        form.setStatus(application.getApplicationStatus());
        form.setApplicantName(application.getMember().getName());

        return form;
    }

    private DonationDetailForm convertToDonationDetailForm(Application application) {
        Donation donation = (Donation) application;
        DonationDetailForm form = new DonationDetailForm();
        AbandonedAnimal animal = donation.getAbandonedAnimal();

        form.setAnimalId(animal.getId());
        form.setFileStoreName(animal.getPicture().getFileStoreName());
        form.setStatus(animal.getStatus());
        form.setSpecies(animal.getSpecies());
        form.setKind(animal.getKind());
        form.setAnimalName(animal.getName());
        form.setAge(animal.getAge());
        form.setWeight(animal.getWeight());

        form.setApplicationId(donation.getId());
        form.setBankType(donation.getBankType());
        form.setBacker(donation.getBacker());
        form.setAccountNumber(donation.getAccountNumber());
        form.setAmount(donation.getAmount());

        return form;
    }

    private TempProtectionDetailForm convertToTempProtectionDetailForm(Application application) {
        TemporaryProtection temporaryProtection = (TemporaryProtection) application;
        TempProtectionDetailForm form = new TempProtectionDetailForm();
        AbandonedAnimal animal = temporaryProtection.getAbandonedAnimal();

        form.setAnimalId(animal.getId());
        form.setFileStoreName(animal.getPicture().getFileStoreName());
        form.setStatus(animal.getStatus());
        form.setSpecies(animal.getSpecies());
        form.setKind(animal.getKind());
        form.setAnimalName(animal.getName());
        form.setAge(animal.getAge());
        form.setWeight(animal.getWeight());

        form.setApplicantName(temporaryProtection.getMember().getName());
        form.setApplicationId(temporaryProtection.getId());
        form.setLocation(temporaryProtection.getLocation());
        form.setJob(temporaryProtection.getJob());
        form.setMarried(temporaryProtection.getMarried());
        form.setPeriod(temporaryProtection.getPeriod());

        return form;
    }

    private AdoptDetailForm convertToAdoptDetailForm(Application application) {
        Adopt adopt = (Adopt) application;
        AdoptDetailForm form = new AdoptDetailForm();
        AbandonedAnimal animal = adopt.getAbandonedAnimal();

        form.setAnimalId(animal.getId());
        form.setFileStoreName(animal.getPicture().getFileStoreName());
        form.setStatus(animal.getStatus());
        form.setSpecies(animal.getSpecies());
        form.setKind(animal.getKind());
        form.setAnimalName(animal.getName());
        form.setAge(animal.getAge());
        form.setWeight(animal.getWeight());

        form.setApplicantName(adopt.getMember().getName());
        form.setApplicationId(adopt.getId());
        form.setLocation(adopt.getLocation());
        form.setJob(adopt.getJob());
        form.setMarried(adopt.getMarried());

        return form;
    }
}

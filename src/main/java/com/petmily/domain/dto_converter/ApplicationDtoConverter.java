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

        if (dtoType.isAssignableFrom(ApplicationListForm.class)) {
            log.info("Application -> ApplicationListForm");
            dto = (T) convertApplicationListForm(application);
        }

        if (dtoType.isAssignableFrom(DonationDetailForm.class)) {
            log.info("Application -> DonationDetailForm");
            dto = (T) convertToDonationDetailForm(application);
        }

        if (dtoType.isAssignableFrom(TempProtectionDetailForm.class)) {
            log.info("Application -> TempProtectionDetailForm");
            dto = (T) convertToTempProtectionDetailForm(application);
        }

        if (dtoType.isAssignableFrom(AdoptDetailForm.class)) {
            log.info("Application -> AdoptDetailForm");
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

        return form;
    }

    private DonationDetailForm convertToDonationDetailForm(Application application) {
        Donation donation = (Donation) application;
        DonationDetailForm form = new DonationDetailForm();
        AbandonedAnimal animal = donation.getAbandonedAnimal();

        form.setFileStoreName(animal.getPicture().getFileStoreName());
        form.setStatus(animal.getStatus());
        form.setSpecies(animal.getSpecies());
        form.setKind(animal.getKind());
        form.setAnimalName(animal.getName());
        form.setAge(animal.getAge());
        form.setWeight(animal.getWeight());

        form.setApplicationId(donation.getId());
        form.setBankType(donation.getBankType());
        form.setDonator(donation.getDonator());
        form.setAccountNumber(donation.getAccountNumber());
        form.setAmount(donation.getAmount());

        return form;
    }

    private TempProtectionDetailForm convertToTempProtectionDetailForm(Application application) {
        TemporaryProtection temporaryProtection = (TemporaryProtection) application;
        TempProtectionDetailForm form = new TempProtectionDetailForm();
        AbandonedAnimal animal = temporaryProtection.getAbandonedAnimal();

        form.setFileStoreName(animal.getPicture().getFileStoreName());
        form.setStatus(animal.getStatus());
        form.setSpecies(animal.getSpecies());
        form.setKind(animal.getKind());
        form.setAnimalName(animal.getName());
        form.setAge(animal.getAge());
        form.setWeight(animal.getWeight());

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

        form.setFileStoreName(animal.getPicture().getFileStoreName());
        form.setStatus(animal.getStatus());
        form.setSpecies(animal.getSpecies());
        form.setKind(animal.getKind());
        form.setAnimalName(animal.getName());
        form.setAge(animal.getAge());
        form.setWeight(animal.getWeight());

        form.setApplicationId(adopt.getId());
        form.setLocation(adopt.getLocation());
        form.setJob(adopt.getJob());
        form.setMarried(adopt.getMarried());

        return form;
    }
}

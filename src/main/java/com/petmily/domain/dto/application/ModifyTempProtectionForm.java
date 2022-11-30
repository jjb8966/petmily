package com.petmily.domain.dto.application;

import com.petmily.domain.enum_type.LocationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ModifyTempProtectionForm {

    private LocationType location;
    private String job;
    private Boolean married;
    private Integer period;

    public ModifyTempProtectionForm(TempProtectionDetailForm form) {
        this.location = form.getLocation();
        this.job = form.getJob();
        this.married = form.getMarried();
        this.period = form.getPeriod();
    }
}

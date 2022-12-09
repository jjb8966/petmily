package com.petmily.domain.dto_converter;

import com.petmily.domain.core.BaseEntity;
import com.petmily.domain.core.Member;
import com.petmily.domain.dto.member.ModifyMemberForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class MemberDtoConverter implements EntityDtoConverter {

    @Override
    public <T> Optional<T> entityToDto(BaseEntity entity, Class<T> dtoType) {
        Member member = (Member) entity;
        T dto = null;

        if (ModifyMemberForm.class.isAssignableFrom(dtoType)) {
            log.info("Member -> ModifyMemberForm");
            dto = (T) convertToModifyMemberForm(member);
        }

        return Optional.ofNullable(dto);
    }

    private ModifyMemberForm convertToModifyMemberForm(Member member) {
        ModifyMemberForm modifyMemberForm = new ModifyMemberForm();

        modifyMemberForm.setLoginId(member.getLoginId());
        modifyMemberForm.setPassword(member.getPassword());
        modifyMemberForm.setName(member.getName());
        modifyMemberForm.setPhoneNumber(member.getPhoneNumber());
        modifyMemberForm.setEmail(member.getEmail());

        return modifyMemberForm;
    }
}

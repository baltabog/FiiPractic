package com.fii.practic.mes.wip.general.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateArtificialDto {
    @Null
    private final String uuid;
    @NotNull
    private final String name;
    @Null
    private final Integer version;
    @Null
    private final String updatedBy;
}

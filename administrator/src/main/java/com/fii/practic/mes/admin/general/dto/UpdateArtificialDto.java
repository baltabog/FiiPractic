package com.fii.practic.mes.admin.general.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateArtificialDto {
    @NotNull
    private final String uuid;
    @NotNull
    private final String name;
    @NotNull
    private final Integer version;
    @Null
    private final String updatedBy;
}

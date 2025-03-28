package com.fii.practic.mes.admin.general.dto;

import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private final String updatedBy;
}

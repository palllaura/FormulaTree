package com.palllaura.formulatree.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO that holds submission request info.
 */
@Getter
@Setter
public class SubmissionRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotEmpty
    private List<String> carOptionKeys;

    @NotNull
    private Boolean hasLicense;

    /**
     * Used for updating existing submission.
     */
    private Long editSubmissionId;
}

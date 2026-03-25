package com.palllaura.formulatree.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO that holds submission response.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionResponse {
    private Boolean valid;
    private Long id;
    private String name;
    private String phone;
    private Boolean hasLicense;
    private List<String> carOptionKeys;
    private List<String> errors = new ArrayList<>();
}

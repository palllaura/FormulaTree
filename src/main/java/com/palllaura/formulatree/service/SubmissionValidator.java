package com.palllaura.formulatree.service;

import com.palllaura.formulatree.dto.SubmissionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Submission validator class.
 */
@Service
public class SubmissionValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubmissionValidator.class);

    public static final String NAME_MISSING = "Ees- ja perekonnanimi puudub.";
    public static final String NAME_INCOMPLETE = "Palun sisestage nii ees- kui perekonnanimi.";
    public static final String PHONE_INVALID = "Kontakttelefon puudub või on ebakorrektne.";
    public static final String CAR_OPTIONS_MISSING = "Vähemalt üks automark peab olema valitud.";
    public static final String LICENSE_MISSING = "Palun märkige, kas Teil on kehtiv juhiluba.";

    /**
     * Validates all fields of a submission request and returns list of errors.
     */
    public List<String> validate(SubmissionRequest req) {
        List<String> errors = new ArrayList<>();

        validateName(req.getName(), errors);
        validatePhone(req.getPhone(), errors);
        validateCarOptions(req.getCarOptionKeys(), errors);
        validateLicense(req.getHasLicense(), errors);

        if (!errors.isEmpty()) {
            LOGGER.warn("Submission validation failed.");
        }

        return errors;
    }

    /**
     * Validate name field.
     * Name field may not be empty and must consist of both first name and last name.
     */
    private void validateName(String name, List<String> errors) {
        if (name == null || name.trim().isEmpty()) {
            errors.add(NAME_MISSING);
            return;
        }

        String[] parts = name.trim().split("\\s+");
        if (parts.length < 2) {
            errors.add(NAME_INCOMPLETE);
        }
    }

    /**
     * Validate phone number.
     * Phone number may only consist on one '+' symbol and numbers.
     * Phone number must be between 5-10 characters long.
     */
    private void validatePhone(String phone, List<String> errors) {
        if (phone == null || !phone.matches("^[+]?[0-9 ]{5,20}$")) {
            errors.add(PHONE_INVALID);
        }
    }

    /**
     * List of chosen car options may not be empty.
     */
    private void validateCarOptions(List<String> keys, List<String> errors) {
        if (keys == null || keys.isEmpty()) {
            errors.add(CAR_OPTIONS_MISSING);
        }
    }

    /**
     * HasLicense boolean must not be null.
     */
    private void validateLicense(Boolean hasLicense, List<String> errors) {
        if (hasLicense == null) {
            errors.add(LICENSE_MISSING);
        }
    }
}
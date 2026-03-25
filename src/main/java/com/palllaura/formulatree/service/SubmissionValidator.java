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
            errors.add("Ees- ja perekonnanimi puudub.");
            return;
        }

        String[] parts = name.trim().split("\\s+");
        if (parts.length < 2) {
            errors.add("Palun sisestage nii ees- kui perekonnanimi.");
        }
    }

    /**
     * Validate phone number.
     * Phone number may only consist on one '+' symbol and numbers.
     * Phone number must be between 5-10 characters long.
     */
    private void validatePhone(String phone, List<String> errors) {
        if (phone == null || !phone.matches("^[+]?[0-9 ]{5,20}$")) {
            errors.add("Kontakttelefon puudub või on ebakorrektne.");
        }
    }

    /**
     * List of chosen car options may not be empty.
     */
    private void validateCarOptions(List<String> keys, List<String> errors) {
        if (keys == null || keys.isEmpty()) {
            errors.add("Vähemalt üks automark peab olema valitud.");
        }
    }

    /**
     * HasLicense boolean must not be null.
     */
    private void validateLicense(Boolean hasLicense, List<String> errors) {
        if (hasLicense == null) {
            errors.add("Palun märkige, kas Teil on kehtiv juhiluba.");
        }
    }
}
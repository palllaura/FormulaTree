package com.palllaura.formulatree.service;

import com.palllaura.formulatree.dto.SubmissionRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubmissionValidatorTest {

    private final SubmissionValidator validator = new SubmissionValidator();

    /**
     * Helper method for valid request.
     */
    private SubmissionRequest validRequest() {
        SubmissionRequest req = new SubmissionRequest();
        req.setName("Test User");
        req.setPhone("+37212345");
        req.setCarOptionKeys(List.of("bmw"));
        req.setHasLicense(true);
        return req;
    }


    @Test
    void shouldReturnErrorWhenNameIsNull() {
        SubmissionRequest req = validRequest();
        req.setName(null);

        List<String> errors = validator.validate(req);

        assertThat(errors).contains("Ees- ja perekonnanimi puudub.");
    }

    @Test
    void shouldReturnErrorWhenNameIsEmpty() {
        SubmissionRequest req = validRequest();
        req.setName("   ");

        List<String> errors = validator.validate(req);

        assertThat(errors).contains("Ees- ja perekonnanimi puudub.");
    }

    @Test
    void shouldReturnErrorWhenOnlyOneNameProvided() {
        SubmissionRequest req = validRequest();
        req.setName("Madis");

        List<String> errors = validator.validate(req);

        assertThat(errors).contains("Palun sisestage nii ees- kui perekonnanimi.");
    }

    @Test
    void shouldReturnErrorWhenPhoneIsNull() {
        SubmissionRequest req = validRequest();
        req.setPhone(null);

        List<String> errors = validator.validate(req);

        assertThat(errors).contains("Kontakttelefon puudub või on ebakorrektne.");
    }

    @Test
    void shouldReturnErrorWhenPhoneIsInvalid() {
        SubmissionRequest req = validRequest();
        req.setPhone("abc123");

        List<String> errors = validator.validate(req);

        assertThat(errors).contains("Kontakttelefon puudub või on ebakorrektne.");
    }

    @Test
    void shouldReturnErrorWhenCarOptionsNull() {
        SubmissionRequest req = validRequest();
        req.setCarOptionKeys(null);

        List<String> errors = validator.validate(req);

        assertThat(errors).contains("Vähemalt üks automark peab olema valitud.");
    }

    @Test
    void shouldReturnErrorWhenCarOptionsEmpty() {
        SubmissionRequest req = validRequest();
        req.setCarOptionKeys(List.of());

        List<String> errors = validator.validate(req);

        assertThat(errors).contains("Vähemalt üks automark peab olema valitud.");
    }

    @Test
    void shouldReturnErrorWhenLicenseIsNull() {
        SubmissionRequest req = validRequest();
        req.setHasLicense(null);

        List<String> errors = validator.validate(req);

        assertThat(errors).contains("Palun märkige, kas Teil on kehtiv juhiluba.");
    }

    @Test
    void shouldNotReturnErrorWhenLicenseIsTrueOrFalse() {
        SubmissionRequest req1 = validRequest();
        req1.setHasLicense(true);

        SubmissionRequest req2 = validRequest();
        req2.setHasLicense(false);

        List<String> errors1 = validator.validate(req1);
        List<String> errors2 = validator.validate(req2);

        assertThat(errors1).isEmpty();
        assertThat(errors2).isEmpty();
    }


    @Test
    void shouldReturnNoErrorsWhenRequestIsValid() {
        SubmissionRequest req = validRequest();

        List<String> errors = validator.validate(req);

        assertThat(errors).isEmpty();
    }

    @Test
    void shouldReturnMultipleErrorsWhenMultipleFieldsInvalid() {
        SubmissionRequest req = new SubmissionRequest();

        List<String> errors = validator.validate(req);

        assertThat(errors).hasSize(4);
    }
}
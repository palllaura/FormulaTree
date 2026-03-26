package com.palllaura.formulatree.service;

import com.palllaura.formulatree.dto.SubmissionRequest;
import com.palllaura.formulatree.dto.SubmissionResponse;
import com.palllaura.formulatree.repository.CarOptionRepository;
import com.palllaura.formulatree.repository.SubmissionRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class SubmissionServiceWithValidatorTest {

    private final SubmissionRepository submissionRepository = mock(SubmissionRepository.class);
    private final CarOptionRepository carOptionRepository = mock(CarOptionRepository.class);
    private final SubmissionValidator validator = new SubmissionValidator();

    private final SubmissionService service =
            new SubmissionService(submissionRepository, carOptionRepository, validator);

    private SubmissionRequest validRequest() {
        SubmissionRequest req = new SubmissionRequest();
        req.setName("Test User");
        req.setPhone("+37212345");
        req.setCarOptionKeys(List.of("bmw"));
        req.setHasLicense(true);
        return req;
    }

    @Test
    void shouldReturnInvalidResponseWhenValidationFails() {
        SubmissionRequest req = new SubmissionRequest();

        SubmissionResponse response = service.createSubmission(req);

        assertThat(response.getValid()).isFalse();
        assertThat(response.getErrors()).isNotEmpty();

        verifyNoInteractions(submissionRepository);
    }

    @Test
    void shouldContainValidationErrorsWhenInvalidRequest() {
        SubmissionRequest req = new SubmissionRequest();

        SubmissionResponse response = service.createSubmission(req);

        assertThat(response.getErrors()).contains(
                SubmissionValidator.NAME_MISSING,
                SubmissionValidator.PHONE_INVALID,
                SubmissionValidator.CAR_OPTIONS_MISSING,
                SubmissionValidator.LICENSE_MISSING
        );
    }

    @Test
    void shouldSaveWhenRequestIsValid() {
        SubmissionRequest req = validRequest();

        service.createSubmission(req);

        verify(submissionRepository).save(any());
    }
}
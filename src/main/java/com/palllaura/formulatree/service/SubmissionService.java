package com.palllaura.formulatree.service;

import com.palllaura.formulatree.dto.SubmissionRequest;
import com.palllaura.formulatree.dto.SubmissionResponse;
import com.palllaura.formulatree.entity.CarOption;
import com.palllaura.formulatree.entity.Submission;
import com.palllaura.formulatree.repository.CarOptionRepository;
import com.palllaura.formulatree.repository.SubmissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

/**
 * Service class to handle all submission-related actions.
 */
@Service
public class SubmissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubmissionService.class);

    private final SubmissionRepository submissionRepository;
    private final CarOptionRepository carOptionRepository;
    private final SubmissionValidator validator;

    /**
     * Service constructor.
     * @param submissionRepository submission repository.
     * @param carOptionRepository  car options repository.
     */
    public SubmissionService(SubmissionRepository submissionRepository,
                             CarOptionRepository carOptionRepository,
                             SubmissionValidator validator) {
        this.submissionRepository = submissionRepository;
        this.carOptionRepository = carOptionRepository;
        this.validator = validator;
    }

    /**
     * Create and save new submission if request is valid.
     */
    @Transactional
    public SubmissionResponse createSubmission(SubmissionRequest request) {
        List<String> errors = validator.validate(request);

        if (!errors.isEmpty()) {
            return invalidResponse(errors);
        }

        Submission submission = buildSubmission(request, new Submission());
        submissionRepository.save(submission);

        LOGGER.info("Successfully created submission with id: {}", submission.getId());

        return toResponse(submission);
    }

    /**
     * Update existing submission if request is valid.
     */
    @Transactional
    public SubmissionResponse updateCurrentSubmission(SubmissionRequest request) {
        if (request.getEditSubmissionId() == null) {
            LOGGER.warn("Missing submission ID");
            return invalidResponse(List.of("Muudatuste salvestamine ebaõnnestus."));
        }

        Optional<Submission> optional =
                submissionRepository.findById(request.getEditSubmissionId());

        if (optional.isEmpty()) {
            LOGGER.warn("Submission not found");
            return invalidResponse(List.of("Muudatuste salvestamine ebaõnnestus."));
        }

        List<String> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return invalidResponse(errors);
        }

        Submission submission = buildSubmission(request, optional.get());
        submissionRepository.save(submission);

        LOGGER.info("Successfully updated submission with id: {}", submission.getId());

        return toResponse(submission);
    }

    /**
     * Common method for creating/updating submission.
     */
    private Submission buildSubmission(SubmissionRequest request, Submission submission) {
        List<CarOption> options =
                carOptionRepository.findAllByKeyIn(request.getCarOptionKeys());

        submission.setName(request.getName().trim());
        submission.setPhone(request.getPhone());
        submission.setCarOptions(new LinkedHashSet<>(options));
        submission.setHasLicense(request.getHasLicense());

        return submission;
    }

    /**
     * Create response from valid submission.
     */
    private SubmissionResponse toResponse(Submission submission) {
        SubmissionResponse response = new SubmissionResponse();
        response.setValid(true);
        response.setId(submission.getId());
        response.setName(submission.getName());
        response.setPhone(submission.getPhone());
        response.setHasLicense(submission.getHasLicense());
        response.setCarOptionKeys(
                submission.getCarOptions()
                        .stream()
                        .map(CarOption::getKey)
                        .toList()
        );
        return response;
    }

    /**
     * Create response for invalid submission.
     */
    private SubmissionResponse invalidResponse(List<String> errors) {
        SubmissionResponse response = new SubmissionResponse();
        response.setValid(false);
        response.setErrors(errors);
        return response;
    }
}
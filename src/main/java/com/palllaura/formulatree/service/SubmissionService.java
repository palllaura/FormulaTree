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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Service class to handle all submission-related actions.
 */
@Service
public class SubmissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubmissionService.class);

    private final SubmissionRepository submissionRepository;
    private final CarOptionRepository carOptionRepository;

    /**
     * Service constructor.
     * @param submissionRepository submission repository.
     * @param carOptionRepository car options repository.
     */
    public SubmissionService(SubmissionRepository submissionRepository,
                             CarOptionRepository carOptionRepository) {
        this.submissionRepository = submissionRepository;
        this.carOptionRepository = carOptionRepository;
    }

    /**
     * Create and save new submission if request is valid.
     */
    @Transactional
    public SubmissionResponse createSubmission(SubmissionRequest request) {
        List<String> errors = new ArrayList<>();

        if (!validateRequest(request, errors)) {
            return invalidResponse(errors);
        }

        List<CarOption> options =
                carOptionRepository.findAllByKeyIn(request.getCarOptionKeys());

        Submission submission = new Submission();
        submission.setName(request.getName().trim());
        submission.setPhone(request.getPhone());
        submission.setCarOptions(new LinkedHashSet<>(options));
        submission.setHasLicense(request.getHasLicense());

        submissionRepository.save(submission);

        LOGGER.info("Successfully created submission with id: {}", submission.getId());

        return toResponse(submission);
    }

    /**
     * Validate request fields.
     */
    private boolean validateRequest(SubmissionRequest req, List<String> errors) {
        return true;
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
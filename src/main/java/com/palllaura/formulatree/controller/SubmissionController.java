package com.palllaura.formulatree.controller;

import com.palllaura.formulatree.dto.SubmissionRequest;
import com.palllaura.formulatree.dto.SubmissionResponse;
import com.palllaura.formulatree.service.SubmissionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/submission")
public class SubmissionController {

    private final SubmissionService service;

    public SubmissionController(SubmissionService service) {
        this.service = service;
    }

    /**
     * Create new submission or update existing one.
     */
    @PostMapping
    public ResponseEntity<SubmissionResponse> submit(
            @Valid @RequestBody SubmissionRequest request) {

        SubmissionResponse response = (request.getEditSubmissionId() == null)
                ? service.createSubmission(request)
                : service.updateCurrentSubmission(request);

        if (Boolean.FALSE.equals(response.getValid())) {
            return ResponseEntity.badRequest().body(response);
        }

        HttpStatus status = (request.getEditSubmissionId() == null)
                ? HttpStatus.CREATED
                : HttpStatus.OK;

        return ResponseEntity.status(status).body(response);
    }
}
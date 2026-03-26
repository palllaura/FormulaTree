package com.palllaura.formulatree.controller;

import com.palllaura.formulatree.dto.SubmissionRequest;
import com.palllaura.formulatree.dto.SubmissionResponse;
import com.palllaura.formulatree.service.SubmissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubmissionControllerTest {

    @Mock
    private SubmissionService service;

    @InjectMocks
    private SubmissionController controller;

    /**
     * Helper method to create valid response.
     */
    private SubmissionResponse validResponse() {
        SubmissionResponse response = new SubmissionResponse();
        response.setValid(true);
        return response;
    }

    /**
     * Helper method to create invalid response.
     */
    private SubmissionResponse invalidResponse() {
        SubmissionResponse response = new SubmissionResponse();
        response.setValid(false);
        return response;
    }

    /**
     * Helper method to create a request with an ID.
     */
    private SubmissionRequest requestWithId() {
        SubmissionRequest request = new SubmissionRequest();
        request.setEditSubmissionId(1L);
        return request;
    }


    @Test
    void shouldReturnCreatedWhenCreateValid() {
        SubmissionResponse response = validResponse();

        when(service.createSubmission(any(SubmissionRequest.class)))
                .thenReturn(response);

        ResponseEntity<SubmissionResponse> result =
                controller.submit(new SubmissionRequest());

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isSameAs(response);

        verify(service).createSubmission(any());
        verify(service, never()).updateCurrentSubmission(any());
    }

    @Test
    void shouldReturnBadRequestWhenCreateInvalid() {
        SubmissionResponse response = invalidResponse();

        when(service.createSubmission(any(SubmissionRequest.class)))
                .thenReturn(response);

        ResponseEntity<SubmissionResponse> result =
                controller.submit(new SubmissionRequest());

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isSameAs(response);

        verify(service).createSubmission(any());
        verify(service, never()).updateCurrentSubmission(any());
    }


    @Test
    void shouldReturnOkWhenUpdateValid() {
        SubmissionResponse response = validResponse();

        when(service.updateCurrentSubmission(any(SubmissionRequest.class)))
                .thenReturn(response);

        SubmissionRequest request = requestWithId();

        ResponseEntity<SubmissionResponse> result =
                controller.submit(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isSameAs(response);

        verify(service).updateCurrentSubmission(any());
        verify(service, never()).createSubmission(any());
    }

    @Test
    void shouldReturnBadRequestWhenUpdateInvalid() {
        SubmissionResponse response = invalidResponse();

        when(service.updateCurrentSubmission(any(SubmissionRequest.class)))
                .thenReturn(response);

        SubmissionRequest request = requestWithId();

        ResponseEntity<SubmissionResponse> result =
                controller.submit(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isSameAs(response);

        verify(service).updateCurrentSubmission(any());
        verify(service, never()).createSubmission(any());
    }

}
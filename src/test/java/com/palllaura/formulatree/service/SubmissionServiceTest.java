package com.palllaura.formulatree.service;

import com.palllaura.formulatree.dto.SubmissionRequest;
import com.palllaura.formulatree.dto.SubmissionResponse;
import com.palllaura.formulatree.entity.CarOption;
import com.palllaura.formulatree.entity.Submission;
import com.palllaura.formulatree.repository.CarOptionRepository;
import com.palllaura.formulatree.repository.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubmissionServiceTest {

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private CarOptionRepository carOptionRepository;

    @InjectMocks
    private SubmissionService service;

    /**
     * Helper method to create a valid request.
     * @return request.
     */
    private SubmissionRequest createValidRequest() {
        SubmissionRequest request = new SubmissionRequest();
        request.setName("Test User");
        request.setPhone("+3720000000");
        request.setCarOptionKeys(List.of("bmw", "audi"));
        request.setHasLicense(true);
        return request;
    }

    /**
     * Helper method to create list of car options.
     * @return list of options.
     */
    private List<CarOption> createCarOptions() {
        CarOption bmw = new CarOption();
        bmw.setKey("bmw");

        CarOption audi = new CarOption();
        audi.setKey("audi");

        return List.of(bmw, audi);
    }

    /**
     * Helper method to mock repository actions.
     * @param request submission request.
     */
    private void mockRepositoryBehaviour(SubmissionRequest request) {
        when(carOptionRepository.findAllByKeyIn(request.getCarOptionKeys()))
                .thenReturn(createCarOptions());

        when(submissionRepository.save(any(Submission.class)))
                .thenAnswer(invocation -> {
                    Submission s = invocation.getArgument(0);
                    s.setId(1L);
                    return s;
                });
    }

    @Test
    void shouldCallCarOptionRepository() {
        SubmissionRequest request = createValidRequest();
        mockRepositoryBehaviour(request);

        service.createSubmission(request);

        verify(carOptionRepository).findAllByKeyIn(request.getCarOptionKeys());
    }

    @Test
    void shouldSaveSubmission() {
        SubmissionRequest request = createValidRequest();
        mockRepositoryBehaviour(request);

        service.createSubmission(request);

        verify(submissionRepository).save(any(Submission.class));
    }

    @Test
    void shouldReturnValidResponse() {
        SubmissionRequest request = createValidRequest();
        mockRepositoryBehaviour(request);

        SubmissionResponse response = service.createSubmission(request);

        assertThat(response.getValid()).isTrue();
    }

    @Test
    void shouldReturnGeneratedId() {
        SubmissionRequest request = createValidRequest();
        mockRepositoryBehaviour(request);

        SubmissionResponse response = service.createSubmission(request);

        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    void shouldMapNameToResponse() {
        SubmissionRequest request = createValidRequest();
        mockRepositoryBehaviour(request);

        SubmissionResponse response = service.createSubmission(request);

        assertThat(response.getName()).isEqualTo("Test User");
    }

    @Test
    void shouldMapPhoneToResponse() {
        SubmissionRequest request = createValidRequest();
        mockRepositoryBehaviour(request);

        SubmissionResponse response = service.createSubmission(request);

        assertThat(response.getPhone()).isEqualTo("+3720000000");
    }

    @Test
    void shouldMapHasLicenseToResponse() {
        SubmissionRequest request = createValidRequest();
        mockRepositoryBehaviour(request);

        SubmissionResponse response = service.createSubmission(request);

        assertThat(response.getHasLicense()).isTrue();
    }

    @Test
    void shouldMapCarOptionKeysToResponse() {
        SubmissionRequest request = createValidRequest();
        mockRepositoryBehaviour(request);

        SubmissionResponse response = service.createSubmission(request);

        assertThat(response.getCarOptionKeys())
                .containsExactlyInAnyOrder("bmw", "audi");
    }

    @Test
    void shouldMapRequestToSubmissionEntity() {
        SubmissionRequest request = createValidRequest();
        mockRepositoryBehaviour(request);

        service.createSubmission(request);

        verify(submissionRepository).save(argThat(submission ->
                submission.getName().equals("Test User") &&
                        submission.getPhone().equals("+3720000000") &&
                        submission.getHasLicense().equals(true) &&
                        submission.getCarOptions().size() == 2
        ));
    }

    @Test
    void shouldUpdateExistingSubmission() {
        SubmissionRequest request = createValidRequest();
        request.setEditSubmissionId(1L);

        Submission existing = new Submission();
        existing.setId(1L);

        when(submissionRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        mockRepositoryBehaviour(request);

        SubmissionResponse response = service.updateCurrentSubmission(request);

        verify(submissionRepository).save(existing);
        assertThat(response.getValid()).isTrue();
    }

    @Test
    void shouldReturnInvalidWhenIdIsMissing() {
        SubmissionRequest request = createValidRequest();

        SubmissionResponse response = service.updateCurrentSubmission(request);

        assertThat(response.getValid()).isFalse();
        verify(submissionRepository, never()).save(any());
    }

    @Test
    void shouldReturnInvalidWhenSubmissionNotFound() {
        SubmissionRequest request = createValidRequest();
        request.setEditSubmissionId(1L);

        when(submissionRepository.findById(1L))
                .thenReturn(Optional.empty());

        SubmissionResponse response = service.updateCurrentSubmission(request);

        assertThat(response.getValid()).isFalse();
        verify(submissionRepository, never()).save(any());
    }


}
package com.palllaura.formulatree.service;

import com.palllaura.formulatree.entity.CarOption;
import com.palllaura.formulatree.repository.CarOptionRepository;
import com.palllaura.formulatree.repository.SubmissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CarOptionDataLoaderIntegrationTest {

    @Autowired
    private CarOptionRepository repository;

    @Autowired
    private CarOptionDataLoader loader;

    @Autowired
    private SubmissionRepository submissionRepository;

    @BeforeEach
    void setUp() {
        submissionRepository.deleteAll();
        repository.deleteAll();
        loader.loadIfEmpty();
    }

    @Test
    void shouldLoadDataIntoDatabase() {
        assertThat(repository.count()).isGreaterThan(0);
    }

    @Test
    void shouldBuildCorrectHierarchy() {
        List<CarOption> all = repository.findAll();

        CarOption bmw = all.stream()
                .filter(o -> o.getName().equals("BMW"))
                .findFirst()
                .orElseThrow();

        CarOption series3 = all.stream()
                .filter(o -> o.getName().equals("3 seeria"))
                .findFirst()
                .orElseThrow();

        assertThat(series3.getParent()).isEqualTo(bmw);
    }

    @Test
    void shouldCalculateLevelsCorrectly() {
        List<CarOption> all = repository.findAll();

        CarOption bmw = all.stream()
                .filter(o -> o.getName().equals("BMW"))
                .findFirst()
                .orElseThrow();

        CarOption series3 = all.stream()
                .filter(o -> o.getName().equals("3 seeria"))
                .findFirst()
                .orElseThrow();

        assertThat(bmw.getLevel()).isZero();
        assertThat(series3.getLevel()).isEqualTo(1);
    }

    @Test
    void shouldLoadThreeLevelHierarchyCorrectly() {
        List<CarOption> all = repository.findAll();

        CarOption model = all.stream()
                .filter(o -> o.getName().equals("318"))
                .findFirst()
                .orElseThrow();

        assertThat(model.getLevel()).isEqualTo(2);
        assertThat(model.getParent()).isNotNull();
        assertThat(model.getParent().getParent()).isNotNull();
    }
}
package com.palllaura.formulatree.service;

import com.palllaura.formulatree.dto.CarOptionJson;
import com.palllaura.formulatree.entity.CarOption;
import com.palllaura.formulatree.repository.CarOptionRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CarOptionDataLoaderTest {

    private final CarOptionDataLoader loader =
            new CarOptionDataLoader(null, null);

    @Test
    void shouldCreateOptionsWithoutHierarchy() {
        List<CarOptionJson> jsonList = List.of(
                new CarOptionJson("bmw", "BMW", null),
                new CarOptionJson("series3", "3 seeria", "bmw")
        );

        Map<String, CarOption> result = loader.createOptions(jsonList);

        assertThat(result).hasSize(2);
        assertThat(result.get("bmw").getName()).isEqualTo("BMW");
        assertThat(result.get("series3").getName()).isEqualTo("3 seeria");
    }

    @Test
    void shouldLinkHierarchyCorrectly() {
        List<CarOptionJson> jsonList = List.of(
                new CarOptionJson("bmw", "BMW", null),
                new CarOptionJson("series3", "3 seeria", "bmw")
        );

        Map<String, CarOption> map = loader.createOptions(jsonList);

        loader.linkHierarchy(jsonList, map);

        CarOption parent = map.get("bmw");
        CarOption child = map.get("series3");

        assertThat(child.getParent()).isEqualTo(parent);
        assertThat(parent.getChildren()).contains(child);
    }

    @Test
    void shouldIgnoreMissingParent() {
        List<CarOptionJson> jsonList = List.of(
                new CarOptionJson("series3", "3 seeria", "bmw") // parent missing
        );

        Map<String, CarOption> map = loader.createOptions(jsonList);

        loader.linkHierarchy(jsonList, map);

        CarOption child = map.get("series3");

        assertThat(child.getParent()).isNull();
    }

    @Test
    void shouldCalculateLevelsCorrectly() {
        CarOption root = new CarOption();
        root.setName("BMW");

        CarOption child = new CarOption();
        child.setParent(root);

        CarOption grandChild = new CarOption();
        grandChild.setParent(child);

        root.setChildren(List.of(child));
        child.setChildren(List.of(grandChild));

        loader.calculateLevels(Map.of(
                "root", root,
                "child", child,
                "grandChild", grandChild
        ));

        assertThat(root.getLevel()).isZero();
        assertThat(child.getLevel()).isEqualTo(1);
        assertThat(grandChild.getLevel()).isEqualTo(2);
    }

    @Test
    void shouldHandleThreeLevelHierarchy() {
        List<CarOptionJson> jsonList = List.of(
                new CarOptionJson("bmw", "BMW", null),
                new CarOptionJson("series3", "3 seeria", "bmw"),
                new CarOptionJson("model318", "318", "series3")
        );

        Map<String, CarOption> map = loader.createOptions(jsonList);

        loader.linkHierarchy(jsonList, map);
        loader.calculateLevels(map);

        assertThat(map.get("bmw").getLevel()).isZero();
        assertThat(map.get("series3").getLevel()).isEqualTo(1);
        assertThat(map.get("model318").getLevel()).isEqualTo(2);
    }

    @Test
    void shouldSkipLoadingWhenDataExists() {
        CarOptionRepository repo = mock(CarOptionRepository.class);
        EntityManager em = mock(EntityManager.class);

        when(repo.count()).thenReturn(1L);

        CarOptionDataLoader loader = spy(new CarOptionDataLoader(repo, em));

        loader.loadIfEmpty();

        verify(loader, never()).loadJson();
        verify(loader, never()).persistAll(any());
    }

    @Test
    void shouldLoadWhenRepositoryEmpty() {
        CarOptionRepository repo = mock(CarOptionRepository.class);
        EntityManager em = mock(EntityManager.class);

        when(repo.count()).thenReturn(0L);

        CarOptionDataLoader loader = spy(new CarOptionDataLoader(repo, em));

        List<CarOptionJson> mockJson = List.of(
                new CarOptionJson("bmw", "BMW", null)
        );

        doReturn(mockJson).when(loader).loadJson();
        doNothing().when(loader).persistAll(any());

        loader.loadIfEmpty();

        verify(loader).loadJson();
        verify(loader).persistAll(any());
    }
}
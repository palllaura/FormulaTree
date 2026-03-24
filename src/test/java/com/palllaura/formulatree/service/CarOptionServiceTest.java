package com.palllaura.formulatree.service;

import com.palllaura.formulatree.dto.CarOptionTreeDto;
import com.palllaura.formulatree.entity.CarOption;
import com.palllaura.formulatree.repository.CarOptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarOptionServiceTest {

    @Mock
    private CarOptionRepository repository;

    @InjectMocks
    private CarOptionService service;

    @Test
    void shouldReturnEmptyListWhenNoData() {
        when(repository.findAll()).thenReturn(List.of());

        List<CarOptionTreeDto> result = service.getCarOptionTree();

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnOnlyRootElements() {
        CarOption bmw = createOption("bmw", "BMW", 0, null);
        CarOption audi = createOption("audi", "Audi", 0, null);

        when(repository.findAll()).thenReturn(List.of(bmw, audi));

        List<CarOptionTreeDto> result = service.getCarOptionTree();

        assertThat(result).hasSize(2);
        assertThat(result).extracting("key")
                .containsExactlyInAnyOrder("bmw", "audi");
    }

    @Test
    void shouldBuildHierarchyCorrectly() {
        CarOption bmw = createOption("bmw", "BMW", 0, null);
        CarOption series3 = createOption("series3", "3 seeria", 1, bmw);

        bmw.getChildren().add(series3);

        when(repository.findAll()).thenReturn(List.of(bmw, series3));

        List<CarOptionTreeDto> result = service.getCarOptionTree();

        CarOptionTreeDto root = result.getFirst();

        assertThat(root.getKey()).isEqualTo("bmw");
        assertThat(root.getChildren()).hasSize(1);
        assertThat(root.getChildren().getFirst().getKey()).isEqualTo("series3");
    }

    @Test
    void shouldMapLevelsCorrectly() {
        CarOption bmw = createOption("bmw", "BMW", 0, null);
        CarOption series3 = createOption("series3", "3 seeria", 1, bmw);

        bmw.getChildren().add(series3);

        when(repository.findAll()).thenReturn(List.of(bmw, series3));

        List<CarOptionTreeDto> result = service.getCarOptionTree();

        CarOptionTreeDto root = result.getFirst();
        CarOptionTreeDto child = root.getChildren().getFirst();

        assertThat(root.getLevel()).isZero();
        assertThat(child.getLevel()).isEqualTo(1);
    }

    /**
     * Helper method to create test CarOption.
     */
    private CarOption createOption(String key, String name, int level, CarOption parent) {
        CarOption option = new CarOption();
        option.setKey(key);
        option.setName(name);
        option.setLevel(level);
        option.setParent(parent);
        return option;
    }
}
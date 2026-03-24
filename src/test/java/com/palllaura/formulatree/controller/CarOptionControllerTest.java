package com.palllaura.formulatree.controller;

import com.palllaura.formulatree.dto.CarOptionTreeDto;
import com.palllaura.formulatree.service.CarOptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarOptionControllerTest {

    @Mock
    private CarOptionService service;

    @InjectMocks
    private CarOptionController controller;

    @Test
    void shouldCallServiceMethod() {
        controller.getCarOptions();
        verify(service, times(1)).getCarOptionTree();
    }

    @Test
    void shouldReturnServiceResult() {
        List<CarOptionTreeDto> expected = List.of(
                new CarOptionTreeDto("bmw", "BMW", 0)
        );

        when(service.getCarOptionTree()).thenReturn(expected);

        List<CarOptionTreeDto> result = controller.getCarOptions();

        Assertions.assertEquals(expected, result);
    }

    @Test
    void shouldReturnCorrectJson() throws Exception {
        CarOptionTreeDto child = new CarOptionTreeDto("series3", "3 seeria", 1);
        CarOptionTreeDto model = new CarOptionTreeDto("318", "318", 2);
        child.getChildren().add(model);

        CarOptionTreeDto root = new CarOptionTreeDto("bmw", "BMW", 0);
        root.getChildren().add(child);

        List<CarOptionTreeDto> expected = List.of(root);

        when(service.getCarOptionTree()).thenReturn(expected);

        List<CarOptionTreeDto> result = controller.getCarOptions();

        ObjectMapper mapper = new ObjectMapper();
        String resultJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);

        String expectedJson = """
                [
                  {
                    "key": "bmw",
                    "name": "BMW",
                    "level": 0,
                    "children": [
                      {
                        "key": "series3",
                        "name": "3 seeria",
                        "level": 1,
                        "children": [
                          {
                            "key": "318",
                            "name": "318",
                            "level": 2,
                            "children": []
                          }
                        ]
                      }
                    ]
                  }
                ]
                """;

        Assertions.assertEquals(
                mapper.readTree(expectedJson),
                mapper.readTree(resultJson)
        );
    }
}
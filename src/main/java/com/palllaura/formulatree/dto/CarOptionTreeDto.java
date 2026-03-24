package com.palllaura.formulatree.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for creating car options tree.
 */
@Getter
@Setter
@NoArgsConstructor
public class CarOptionTreeDto {

    private String key;
    private String name;
    private int level;
    private List<CarOptionTreeDto> children = new ArrayList<>();

    public CarOptionTreeDto(String key, String name, int level) {
        this.key = key;
        this.name = name;
        this.level = level;
    }

}
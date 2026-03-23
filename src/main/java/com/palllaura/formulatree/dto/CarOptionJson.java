package com.palllaura.formulatree.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO representing car option structure from JSON.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarOptionJson {
    private String key;
    private String name;
    private String parent;

}

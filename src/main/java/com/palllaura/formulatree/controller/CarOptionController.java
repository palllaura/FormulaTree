package com.palllaura.formulatree.controller;

import com.palllaura.formulatree.dto.CarOptionTreeDto;
import com.palllaura.formulatree.service.CarOptionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for car options.
 */
@RestController
@RequestMapping("/api/car-options")
public class CarOptionController {

    private final CarOptionService service;

    public CarOptionController(CarOptionService service) {
        this.service = service;
    }

    /**
     * Returns full car options tree.
     */
    @GetMapping
    public List<CarOptionTreeDto> getCarOptions() {
        return service.getCarOptionTree();
    }
}
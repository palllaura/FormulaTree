package com.palllaura.formulatree.service;

import com.palllaura.formulatree.dto.CarOptionTreeDto;
import com.palllaura.formulatree.entity.CarOption;
import com.palllaura.formulatree.repository.CarOptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CarOptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarOptionService.class);
    private final CarOptionRepository repository;

    /**
     * Service constructor.
     * @param repository CarOptionRepository.
     */
    public CarOptionService(CarOptionRepository repository) {
        this.repository = repository;
    }

    /**
     * Construct a hierarchy tree of all car options in the database.
     * @return List of DTOs.
     */
    @Transactional(readOnly = true)
    public List<CarOptionTreeDto> getCarOptionTree() {
        List<CarOption> all = repository.findAll();

        List<CarOption> roots = all.stream()
                .filter(o -> o.getParent() == null)
                .toList();

        Map<String, CarOptionTreeDto> map = new HashMap<>();

        for (CarOption option : all) {
            CarOptionTreeDto dto = new CarOptionTreeDto(
                    option.getKey(),
                    option.getName(),
                    option.getLevel()
            );
            map.put(option.getKey(), dto);
        }

        for (CarOption option : all) {
            CarOptionTreeDto dto = map.get(option.getKey());

            for (CarOption child : option.getChildren()) {
                CarOptionTreeDto childDto = map.get(child.getKey());
                if (childDto != null) dto.getChildren().add(childDto);
            }
        }

        LOGGER.info("Built car option tree with {} root(s) and {} total options",
                roots.size(), all.size());
        return roots.stream()
                .map(root -> map.get(root.getKey()))
                .toList();
    }

}

package com.palllaura.formulatree.repository;

import com.palllaura.formulatree.entity.CarOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CarOptionRepository extends JpaRepository<CarOption, Long> {
    List<CarOption> findAllByKeyIn(Collection<String> keys);
}

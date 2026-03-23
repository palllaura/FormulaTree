package com.palllaura.formulatree.repository;

import com.palllaura.formulatree.entity.CarOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarOptionRepository extends JpaRepository<CarOption, Long> {
}

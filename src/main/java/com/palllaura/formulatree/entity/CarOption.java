package com.palllaura.formulatree.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "car_option")
@Getter
@Setter
public class CarOption {

    /**
     * Unique car option ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique key to link parent-child relationships in JSON.
     */
    @Column(nullable = false, unique = true)
    private String key;

    /**
     * Car option name.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Hierarchy level (0 = brand, 1 = series, 2 = model).
     */
    @Column(nullable = false)
    private int level;

    /**
     * Parent option for hierarchy (optional).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private CarOption parent;


    /**
     * List with relevant children options.
     */
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarOption> children = new ArrayList<>();

}

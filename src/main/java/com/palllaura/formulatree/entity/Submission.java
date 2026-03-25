package com.palllaura.formulatree.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "submission")
@Getter
@Setter
public class Submission {

    /**
     * Unique submission ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User full name.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Phone number.
     */
    @Column(nullable = false)
    private String phone;

    /**
     * Whether user has driving license.
     */
    @Column(nullable = false)
    private Boolean hasLicense;

    /**
     * Selected car options.
     */
    @ManyToMany
    @JoinTable(
            name = "submission_car_option",
            joinColumns = @JoinColumn(name = "submission_id"),
            inverseJoinColumns = @JoinColumn(name = "car_option_id")
    )
    private Set<CarOption> carOptions = new HashSet<>();
}
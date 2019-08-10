package ru.mosolov.gku.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", nullable = false)
    private Integer id;

    @Column(name="name", nullable = false)
    private String name;
}

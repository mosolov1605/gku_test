package ru.mosolov.gku.models;

import lombok.Data;

import javax.persistence.*;

@MappedSuperclass
@Data
public class BaseDao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", nullable = false)
    private Integer id;

    @Column(name="name", nullable = false)
    private String name;

    @Transient
    private Boolean success;

    @Transient
    private String errorMessage;
}

package ru.mosolov.gku.models;
import lombok.Data;

import javax.persistence.*;

@MappedSuperclass
@Data
abstract class BaseDao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", nullable = false)
    private Integer id;

    @Column(name="name", nullable = false)
    private String name;

    @Transient
    private Boolean success = true;

    @Transient
    private String errorMessage;
}

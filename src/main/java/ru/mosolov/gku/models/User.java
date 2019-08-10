package ru.mosolov.gku.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "usr")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", nullable = false)
    private Integer id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="password", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "company_id")
    Company company;
}

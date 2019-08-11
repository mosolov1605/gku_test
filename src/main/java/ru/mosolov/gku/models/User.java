package ru.mosolov.gku.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "usr")
@Data
public class User extends BaseDao{

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="role", nullable = false)
    private String role;

    @ManyToOne
    @JoinColumn(name = "company_id")
    Company company;
}

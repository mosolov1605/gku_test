package ru.mosolov.gku.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name="name", nullable = false)
    private String name;

    @Lob
    @Column(name="txt")
    private String text;

    @Column(name="date_create", nullable = false)
    private LocalDateTime dateCreate;

    @Column(name="date_close")
    private LocalDateTime dateClose;

    @Column(name="confirm_company")
    private Boolean confirmCompany;

    @Column(name="confirm_counter_company")
    private Boolean confirmCounterCompany;

    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id", nullable=false)
    private Company company;

    @ManyToOne(optional = false)
    @JoinColumn(name = "counter_company_id", nullable=false)
    private Company counterCompany;
}

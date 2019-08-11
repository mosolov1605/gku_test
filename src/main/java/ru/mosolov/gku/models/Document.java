package ru.mosolov.gku.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Document extends BaseDao{

    @Lob
    @Column(name="txt")
    private String text;

    @Column(name="date_create", nullable = false)
    private LocalDateTime dateCreate;

    @Column(name="date_close")
    private LocalDateTime dateClose;

    @Column(name="date_delete")
    private LocalDateTime dateDelete;

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

    public boolean isClosed() {
        return dateClose != null;
    }

    public boolean isDeleted() {
        return dateDelete != null;
    }

    public boolean isOpen() {
        return !isClosed() && !isDeleted();
    }

    public boolean isLockRemove() {
        return isOpen() && (confirmCompany ^ confirmCounterCompany);
    }

    public Company getHolderDoc() {
        if (dateDelete == null && confirmCompany) {
            return counterCompany;
        }
        return company;
    }
}

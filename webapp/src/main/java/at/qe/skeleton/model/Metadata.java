package at.qe.skeleton.model;

import jakarta.persistence.*;
import java.time.LocalDate;


/**
 * Abstract Metadata to save and retrieve creatinDate and updateDate of entities.
 */
@MappedSuperclass
public abstract class Metadata {

    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate createDate;
    @Column(columnDefinition = "DATE")
    private LocalDate updateDate;

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }
}

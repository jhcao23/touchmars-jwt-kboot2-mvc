package technology.touchmars.template.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditableBaseEntity {

	@LastModifiedDate
    @Column(name = "modified_dt")
//    @Temporal(TemporalType.TIMESTAMP)
    private Instant modifiedDt;

	@LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;

    @CreatedDate
    @Column(name = "created_dt")
//    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdDt;

    @CreatedBy
    @Column(name = "created_by")    
    private String createdBy;

}

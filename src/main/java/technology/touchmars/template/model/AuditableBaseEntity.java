package technology.touchmars.template.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditableBaseEntity {

	@LastModifiedDate
    @Column(name = "modified_dt")
//    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime modifiedDt;

	@LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;

    @CreatedDate
    @Column(name = "created_dt")
//    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime createdDt;

    @CreatedBy
    @Column(name = "created_by")    
    private String createdBy;

}

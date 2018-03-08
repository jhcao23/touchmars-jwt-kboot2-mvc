/**
 * This file was generated by the Jeddict
 */
package technology.touchmars.template.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false) 
@Entity
@Table(name = "Msg_File")
public class MsgFile extends AuditableBaseEntity {

    @Column(name = "id", nullable = false)
    @Id @GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, targetEntity = File.class)
    @JoinColumn(name = "FILE_ID", referencedColumnName = "ID")
    private File file;

}
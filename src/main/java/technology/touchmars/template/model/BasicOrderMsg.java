/**
 * This file was generated by the Jeddict
 */
package technology.touchmars.template.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false) 
@MappedSuperclass
public class BasicOrderMsg  extends AuditableBaseEntity {

    @Column(name = "id", nullable = false)
    @Id 
    @GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")	
    private Long id;

    @Column(name = "content", length = 2147483647)
    @Lob
    @Basic
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = MsgFile.class)
    @JoinColumn(name = "FILE_ID", referencedColumnName = "ID")
    private MsgFile file;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = TouchUser.class)
    @JoinColumn(name = "TARGET_USER_ID", referencedColumnName = "ID")
    private TouchUser targetUser;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = TouchUser.class)
    @JoinColumn(name = "OWNER_ID", referencedColumnName = "ID")
    private TouchUser owner;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = MsgType.class)
    @JoinColumn(name = "TYPE_ID", referencedColumnName = "ID")
    private MsgType type;

}
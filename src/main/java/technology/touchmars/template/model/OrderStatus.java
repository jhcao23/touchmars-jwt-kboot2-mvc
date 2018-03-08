/**
 * This file was generated by the Jeddict
 */
package technology.touchmars.template.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false) @Entity
@Table(name = "Order_Status")
public class OrderStatus {

    @Column(name = "id", nullable = false)
    @Id @GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	
    private Integer id;

    @Column(name = "name")
    @Basic
    private String name;

    @Column(name = "code")
    @Basic
    private String code;

    @Column(name = "rank")
    @Basic
    private Integer rank;

}
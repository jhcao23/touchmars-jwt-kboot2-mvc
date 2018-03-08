/**
 * This file was generated by the Jeddict
 */
package technology.touchmars.template.model;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false) @Entity
@Table(name = "Country")
public class Country {

    @Column(name = "id", nullable = false)
    @Id @GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	
    private Long id;

    @Column(name = "name")
    @Basic
    private String name;

    @Column(name = "code")
    @Basic
    private String code;

    @Column(name = "phone_code")
    @Basic
    private Integer phoneCode;

    @OneToMany(targetEntity = Province.class, mappedBy = "country")
    private List<Province> provinceCollection;

}
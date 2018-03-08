/**
 * This file was generated by the Jeddict
 */
package technology.touchmars.template.model;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false) 
@Entity
@Table(name = "Province")
public class Province {

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

    @Column(name = "territory")
    @Basic
    private Boolean territory;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, targetEntity = Country.class)
    @JoinColumn(name = "COUNTRY_ID", referencedColumnName = "ID")
    private Country country;

    @OneToMany(targetEntity = City.class, mappedBy = "province")
    private List<City> cityCollection;

}
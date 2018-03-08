/**
 * This file was generated by the Jeddict
 */
package technology.touchmars.template.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Data
@EqualsAndHashCode(callSuper=false, exclude="touchUser")
@ToString(callSuper=false, exclude="touchUser")
@Entity
@Table(name = "Account", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id"})})
public class Account extends AuditableBaseEntity {

    @Column(name = "id", nullable = false)
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(name = "cell")
    @Basic
    private String cell;

    @Column(name = "email", nullable = false)
    @Basic(optional = false)
    private String username;

    @Column(name = "password", nullable = false)
    @Basic(optional = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @OneToOne(optional = false, fetch = FetchType.EAGER, targetEntity = TouchUser.class)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private TouchUser touchUser;

    public Account() {}

    public Account(String email, String password, String firstName, String lastName) {
        super();
        this.username = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }



}
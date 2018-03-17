package technology.touchmars.template.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "Touch_User_Contact")
public class UserContact {

    @Column(name = "id", nullable = false)
    @Id @GeneratedValue(strategy=GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, targetEntity = Contact.class)
    @JoinColumn(name = "Contact_Id", referencedColumnName = "ID")
    private Contact contact;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, targetEntity = TouchUser.class)
    @JoinColumn(name = "TOUCH_USER_ID", referencedColumnName = "ID")
    private TouchUser touchUser;
}

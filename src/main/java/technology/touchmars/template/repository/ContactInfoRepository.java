package technology.touchmars.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import technology.touchmars.template.model.Contact;
import technology.touchmars.template.model.ContactInfo;

import java.util.List;

public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {

    public List<ContactInfo> findByContact(Contact contact);

    public ContactInfo findTopByContactOrderByRankDesc(Contact contact);

}

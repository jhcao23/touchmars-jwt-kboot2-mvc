package technology.touchmars.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import technology.touchmars.template.model.UserContact;

@Repository
public interface UserContactRepository extends JpaRepository<UserContact, Long> {
}

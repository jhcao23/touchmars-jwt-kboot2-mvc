package technology.touchmars.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import technology.touchmars.template.model.TouchUser;
import technology.touchmars.template.model.UserContact;

import java.util.List;

@Repository
public interface UserContactRepository extends JpaRepository<UserContact, Long> {

    public List<UserContact> findByTouchUser(TouchUser touchUser);

    public UserContact findTopByTouchUser(TouchUser touchUser);
}

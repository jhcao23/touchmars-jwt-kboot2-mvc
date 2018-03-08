package technology.touchmars.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import technology.touchmars.template.model.TouchUser;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<TouchUser, Long> {

	public Optional<TouchUser> findByAccountUsername(String username);
	public Optional<TouchUser> findByHashId(String hashId);
	public Optional<TouchUser> findByHashIdOrAccountUsername(String hashId, String username);

}



package technology.touchmars.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import technology.touchmars.template.model.ConnectionProperty;
import technology.touchmars.template.model.EmailInterface;
import technology.touchmars.template.model.TouchUser;
import technology.touchmars.template.model.UserConnection;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserConnectionRepository extends JpaRepository<UserConnection, Long> {

	@Query("select touchUser.id from UserConnection uc where uc.providerId = ?1 and uc.providerUserId in ?2")
	public List<Integer> findUserIdsByProviderIdAndProviderUserIds(String providerId, Set<String> providerUserIds);
	
	@Query("select u.hashId from UserConnection uc left join uc.touchUser u where uc.providerId = ?1 and uc.providerUserId in ?2")
	public List<String> findUserHashIdsByProviderIdAndProviderUserIds(String providerId, Set<String> providerUserIds);
	
	@Query("select u.hashId from UserConnection uc left join uc.touchUser u where uc.providerId = ?1 and uc.providerUserId = ?2")
	public List<String> findUserHashIdsByProviderIdAndProviderUserId(String providerId, String providerUserId);

	public List<UserConnection> findByEmailAndProviderId(String email, String providerId);

	public List<UserConnection> findByEmailInAndProviderId(Collection<String> emails, String providerId);

	public List<ConnectionProperty> findByTouchUserIdAndProviderIdOrderByRankAsc(Long id, String providerId);
	
	public Optional<ConnectionProperty> findTopByTouchUserIdAndProviderIdOrderByRankAsc(Long id, String providerId);
	
	public List<ConnectionProperty> findByTouchUserIdOrderByProviderIdAscRankAsc(Long id);
	
	public Optional<UserConnection> findByProviderIdAndProviderUserId(String providerId, String providerUserId);
	
	public Optional<UserConnection> findByTouchUserIdAndProviderIdAndProviderUserId(Long id, String providerId, String providerUserId);
	
	@Query("select coalesce(max(uc.rank) + 1, 1) as rank from UserConnection uc where uc.touchUser.id = ?1 and uc.providerId = ?2")
	public Integer findNextRankByTouchUserIdAndProviderId(Long id, String providerId);
		
	@Transactional
	public void deleteByTouchUserIdAndProviderId(Long id, String providerId);	
	
	@Transactional
	public Integer deleteByTouchUserIdAndProviderIdAndProviderUserId(Long id, String providerId, String providerUserId);

	// Projection
	public List<EmailInterface> findEmailByProviderIdAndTouchUserIn(String providerId, Collection<TouchUser> touchUsers);

}



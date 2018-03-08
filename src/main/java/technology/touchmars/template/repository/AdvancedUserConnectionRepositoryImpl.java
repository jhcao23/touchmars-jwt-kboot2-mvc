package technology.touchmars.template.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;
import technology.touchmars.template.model.UserConnection;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

@Repository
public class AdvancedUserConnectionRepositoryImpl 
	extends SimpleJpaRepository<UserConnection, Long>
	implements AdvancedUserConnectionRepository {
	
	private EntityManager entityManager;

	@Autowired
	public AdvancedUserConnectionRepositoryImpl(EntityManager em) {
		super(UserConnection.class, em);
		this.entityManager = em;
	}

	public List<UserConnection> findByUserIdAndMap(Long userId, MultiValueMap<String, String> providerUsers){
		
		String query = 
			"SELECT uc "
			+ "	FROM UserConnection uc "
			+ "	where uc.touchUser.id = :userId "
			+ "	AND ( "
		;
		
		TypedQuery<UserConnection> q = entityManager.createQuery(query, UserConnection.class);
		q = q.setParameter("userId", userId);
		for (Iterator<Entry<String, List<String>>> it = providerUsers.entrySet().iterator(); it.hasNext();) {
			Entry<String, List<String>> entry = it.next();
			String providerId = entry.getKey();
			query+=" 	(	providerId = :providerId_"+providerId+" "
					+ "			and "
					+ "		providerUserId in (:providerUserIds_"+providerId+") 	"
					+"	)	";
			if (it.hasNext()) {
				query += "	OR	";
			}
			q = q.setParameter("providerId_"+providerId, providerId);
			q = q.setParameter("providerUserIds_"+providerId, entry.getValue());
		}
		query += " 	)	"
				+ "	ORDER BY providerId, rank ";
		return q.getResultList();
	}
	
}

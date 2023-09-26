package eu.fbk.dslab.playful.engine.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import eu.fbk.dslab.playful.engine.security.UserRole;

@Repository
public interface UserRoleRepository extends MongoRepository<UserRole, String> {
	public List<UserRole> findByPreferredUsername(String preferredUsername);
	
	@Query("{$or:[{'preferredUsername':{$regex:?0,$options:'i'}}, "
			+ "{'role':{$regex:?0,$options:'i'}}, "
			+ "{'entityId':{$regex:?0,$options:'i'}}]}")
	public Page<UserRole> findByText(String text, Pageable pageRequest);

}

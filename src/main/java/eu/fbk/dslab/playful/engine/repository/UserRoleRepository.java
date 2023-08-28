package eu.fbk.dslab.playful.engine.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.fbk.dslab.playful.engine.security.UserRole;

@Repository
public interface UserRoleRepository extends MongoRepository<UserRole, String> {

	List<UserRole> findByPreferredUsername(String preferredUsername);
}

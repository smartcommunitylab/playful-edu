package eu.fbk.dslab.playful.engine.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.fbk.dslab.playful.engine.model.Group;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
	public List<Group> findByIdIn(List<String> ids);
	public Group findOneByDomainIdAndExtId(String domainId, String extId);
}

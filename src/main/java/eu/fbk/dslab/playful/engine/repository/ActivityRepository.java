package eu.fbk.dslab.playful.engine.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.fbk.dslab.playful.engine.model.Activity;

@Repository
public interface ActivityRepository extends MongoRepository<Activity, String> {
	public List<Activity> findByIdIn(List<String> ids);
	public Page<Activity> findByDomainId(String domainId, Pageable pageRequest);
	public Page<Activity> findByComposedActivityId(String composedActivityId, Pageable pageRequest);
}

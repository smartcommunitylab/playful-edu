package eu.fbk.dslab.playful.engine.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.fbk.dslab.playful.engine.model.Activity;

@Repository
public interface ActivityRepository extends MongoRepository<Activity, String> {
	public List<Activity> findByIdIn(List<String> ids);
	public List<Activity> findByComposedActivityId(String composedActivityId);
}

package eu.fbk.dslab.playful.engine.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.fbk.dslab.playful.engine.model.ComposedActivity;

@Repository
public interface ComposedActivityRepository extends MongoRepository<ComposedActivity, String> {
	public List<ComposedActivity> findByIdIn(List<String> ids);
	public List<ComposedActivity> findByLearningFragmentId(String learningFragmentId);
}

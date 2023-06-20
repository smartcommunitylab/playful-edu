package eu.fbk.dslab.playful.engine.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.fbk.dslab.playful.engine.model.ComposedActivity;

@Repository
public interface ComposedActivityRepository extends MongoRepository<ComposedActivity, String> {
	public List<ComposedActivity> findByIdIsIn(String[] ids);
	public Page<ComposedActivity> findByDomainId(String domainId, Pageable pageRequest);
	public Page<ComposedActivity> findByLearningFragmentId(String learningFragmentId, Pageable pageRequest);
}

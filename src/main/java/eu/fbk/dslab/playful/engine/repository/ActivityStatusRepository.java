package eu.fbk.dslab.playful.engine.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.fbk.dslab.playful.engine.model.ActivityStatus;

@Repository
public interface ActivityStatusRepository extends MongoRepository<ActivityStatus, String> {
	public List<ActivityStatus> findByIdIn(List<String> ids);
	public Page<ActivityStatus> findByDomainId(String domainId, Pageable pageRequest);
	public ActivityStatus findByLearningScenarioIdAndLearnerIdAndExternalActivityId(String learningScenarioId, String learnerId, String externalActivityId);
}

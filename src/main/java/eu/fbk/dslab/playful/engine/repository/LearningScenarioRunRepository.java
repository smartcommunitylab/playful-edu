package eu.fbk.dslab.playful.engine.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.fbk.dslab.playful.engine.model.LearningScenarioRun;

@Repository
public interface LearningScenarioRunRepository extends MongoRepository<LearningScenarioRun, String> {
	public List<LearningScenarioRun> findByIdIsIn(String[] ids);
	public Page<LearningScenarioRun> findByDomainId(String domainId, Pageable pageRequest);
	public LearningScenarioRun findByLearningScenarioIdAndLearnerId(String learningScenarioId, String learnerId);
}

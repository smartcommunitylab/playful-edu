package eu.fbk.dslab.playful.engine.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.fbk.dslab.playful.engine.model.LearningScenario;

@Repository
public interface LearningScenarioRepository extends MongoRepository<LearningScenario, String> {
	public List<LearningScenario> findByIdIn(List<String> ids);
	public Page<LearningScenario> findByDomainId(String domainId, Pageable pageRequest);
}

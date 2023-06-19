package eu.fbk.dslab.playful.engine.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.fbk.dslab.playful.engine.model.LearningModule;

@Repository
public interface LearningModuleRepository extends MongoRepository<LearningModule, String> {
	public List<LearningModule> findByIdIsIn(String[] ids);
	public Page<LearningModule> findByDomainId(String domainId, Pageable pageRequest);
	public Page<LearningModule> findByLearningScenarioId(String learningScenarioId, Pageable pageRequest);
}

package eu.fbk.dslab.playful.engine.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.fbk.dslab.playful.engine.model.LearningModule;

@Repository
public interface LearningModuleRepository extends MongoRepository<LearningModule, String> {
	public List<LearningModule> findByIdIn(List<String> ids);
	public List<LearningModule> findByLearningScenarioId(String learningScenarioId, Sort sort);
}

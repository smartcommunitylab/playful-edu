package eu.fbk.dslab.playful.engine.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.fbk.dslab.playful.engine.model.LearningFragment;

@Repository
public interface LearningFragmentRepository extends MongoRepository<LearningFragment, String> {
	public List<LearningFragment> findByIdIn(List<String> ids);
	public Page<LearningFragment> findByDomainId(String domainId, Pageable pageRequest);
	public Page<LearningFragment> findByLearningModuleId(String learningModuleId, Pageable pageRequest);
	public LearningFragment findFirstByLearningModuleId(String learningModuleId);
}

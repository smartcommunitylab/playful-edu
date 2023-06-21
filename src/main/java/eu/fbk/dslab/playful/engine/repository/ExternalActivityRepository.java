package eu.fbk.dslab.playful.engine.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.fbk.dslab.playful.engine.model.ExternalActivity;

@Repository
public interface ExternalActivityRepository extends MongoRepository<ExternalActivity, String> {
	public List<ExternalActivity> findByIdIn(List<String> ids);
	public Page<ExternalActivity> findByDomainId(String domainId, Pageable pageRequest);
}

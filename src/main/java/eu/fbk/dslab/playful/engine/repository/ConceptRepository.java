package eu.fbk.dslab.playful.engine.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.fbk.dslab.playful.engine.model.Concept;

@Repository
public interface ConceptRepository extends MongoRepository<Concept, String> {
	public List<Concept> findByIdIsIn(String[] ids);
	public Page<Concept> findByDomainId(String domainId, Pageable pageRequest);
}

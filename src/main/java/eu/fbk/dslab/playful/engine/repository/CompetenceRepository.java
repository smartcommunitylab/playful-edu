package eu.fbk.dslab.playful.engine.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.fbk.dslab.playful.engine.model.Competence;

@Repository
public interface CompetenceRepository extends MongoRepository<Competence, String> {
	public List<Competence> findByIdIsIn(String[] ids);
	public Page<Competence> findByDomainId(String domainId, Pageable pageRequest);
}

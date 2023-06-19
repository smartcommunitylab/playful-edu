package eu.fbk.dslab.playful.engine.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.fbk.dslab.playful.engine.model.Educator;

@Repository
public interface EducatorRepository extends MongoRepository<Educator, String> {
	public List<Educator> findByIdIsIn(String[] ids);
	public Page<Educator> findByDomainId(String domainId, Pageable pageRequest);
}

package eu.fbk.dslab.playful.engine.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.fbk.dslab.playful.engine.model.Domain;

@Repository
public interface DomainRepository extends MongoRepository<Domain, String> {
	public List<Domain> findByIdIsIn(String[] ids);
}

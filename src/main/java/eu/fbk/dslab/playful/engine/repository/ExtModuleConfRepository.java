package eu.fbk.dslab.playful.engine.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import eu.fbk.dslab.playful.engine.integration.ExtModuleConf;

public interface ExtModuleConfRepository extends MongoRepository<ExtModuleConf, String> {

}

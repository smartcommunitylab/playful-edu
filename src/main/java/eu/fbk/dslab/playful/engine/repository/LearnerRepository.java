package eu.fbk.dslab.playful.engine.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import eu.fbk.dslab.playful.engine.model.Learner;

@Repository
public interface LearnerRepository extends MongoRepository<Learner, String> {
	public List<Learner> findByIdIn(List<String> ids);
	public Page<Learner> findByDomainId(String domainId, Pageable pageRequest);
	public Learner findOneByDomainIdAndNickname(String domainId, String nickname);
	
	@Query("{'domainId':?0, $or:[{'firstname':{$regex:?1,$options:'i'}}, "
			+ "{'lastname':{$regex:?1,$options:'i'}}, "
			+ "{'email':{$regex:?1,$options:'i'}}, "
			+ "{'nickname':{$regex:?1,$options:'i'}}]}")
	public Page<Learner> findByDomainIdAndText(String domainId, String text, Pageable pageRequest);
}

package eu.fbk.dslab.playful.engine.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import eu.fbk.dslab.playful.engine.model.Educator;

@Repository
public interface EducatorRepository extends MongoRepository<Educator, String> {
	public List<Educator> findByIdIn(List<String> ids);
	public Page<Educator> findByDomainId(String domainId, Pageable pageRequest);
	public Educator findOneByDomainIdAndNickname(String domainId, String nickname);
	public Educator findOneByDomainIdAndEmail(String domainId, String email);
	
	@Query("{'domainId':?0, $or:[{'firstname':{$regex:?1,$options:'i'}}, "
			+ "{'lastname':{$regex:?1,$options:'i'}}, "
			+ "{'email':{$regex:?1,$options:'i'}}, "
			+ "{'nickname':{$regex:?1,$options:'i'}}]}")
	public Page<Educator> findByDomainIdAndText(String domainId, String text, Pageable pageRequest);
}

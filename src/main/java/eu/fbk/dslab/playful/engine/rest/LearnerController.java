package eu.fbk.dslab.playful.engine.rest;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.fbk.dslab.playful.engine.exception.EntityException;
import eu.fbk.dslab.playful.engine.exception.UnauthorizedException;
import eu.fbk.dslab.playful.engine.model.Learner;
import eu.fbk.dslab.playful.engine.repository.LearnerRepository;
import eu.fbk.dslab.playful.engine.security.SecurityHelper;
import eu.fbk.dslab.playful.engine.security.UserRole.Role;

@RestController
public class LearnerController {
	@Autowired
	LearnerRepository learnerRepository;
	
	@Autowired
	SecurityHelper securityHelper;
	
	@GetMapping("/api/learners")
	public Page<Learner> getList(
			@RequestParam(required = false) List<String> ids,
			@RequestParam(required = false) String domainId,
			@RequestParam(required = false) String text,
			@ParameterObject Pageable pageRequest) throws Exception {
		if(ids != null) {
			List<Learner> list = learnerRepository.findByIdIn(ids);
			return new PageImpl<>(list);
		} else if(domainId != null) {
			securityHelper.checkRole(domainId, Role.domain, Role.educator);
			if(text != null) {
				return learnerRepository.findByDomainIdAndText(domainId, text, pageRequest);
			} else {
				return learnerRepository.findByDomainId(domainId, pageRequest);	
			}
		}
		return learnerRepository.findAll(pageRequest);
	}
	
	@GetMapping("/api/learners/{id}")
	public Learner getOne(@PathVariable String id) throws Exception {
		Learner entity = learnerRepository.findById(id).orElse(null);
		if(entity != null) {
			securityHelper.checkRole(entity.getDomainId(), Role.domain);
		}
		return entity;
	}
	
	@PostMapping("/api/learners")
	public Learner create(@RequestBody Learner learner) throws Exception {
		securityHelper.checkRole(learner.getDomainId(), Role.domain);
		return learnerRepository.save(learner);
	}
	
	@PutMapping("/api/learners/{id}")
	public Learner update(@PathVariable String id,
			@RequestBody Learner learner) throws Exception {
		securityHelper.checkRole(learner.getDomainId(), Role.domain);
		Learner l = learnerRepository.findById(id).orElse(null);
		if(l == null) {
			throw new EntityException("entity not found");
		}
		if(!l.getDomainId().equals(learner.getDomainId())) {
			throw new UnauthorizedException("role not found");
		}
		learner.setId(id);
		return learnerRepository.save(learner);
	}
	
	@DeleteMapping("/api/learners/{id}")
	public Learner delete(@PathVariable String id) throws Exception {
		Learner learner = learnerRepository.findById(id).orElse(null);
		if(learner != null) {
			securityHelper.checkRole(learner.getDomainId(), Role.domain);
			learnerRepository.deleteById(id);
		}
		return learner;
	}

}

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

import eu.fbk.dslab.playful.engine.manager.DataManager;
import eu.fbk.dslab.playful.engine.exception.EntityException;
import eu.fbk.dslab.playful.engine.exception.UnauthorizedException;
import eu.fbk.dslab.playful.engine.manager.EntityManager;
import eu.fbk.dslab.playful.engine.model.LearningScenario;
import eu.fbk.dslab.playful.engine.repository.LearningScenarioRepository;
import eu.fbk.dslab.playful.engine.security.SecurityHelper;
import eu.fbk.dslab.playful.engine.security.UserRole.Role;

@RestController
public class LearningScenarioController {
	@Autowired
	LearningScenarioRepository learningScenarioRepository;
	
	@Autowired
	DataManager dataManager;
	
	@Autowired
	SecurityHelper securityHelper;
	
	@Autowired
	EntityManager entityManager;
	
	@GetMapping("/api/scenarios")
	public Page<LearningScenario> getList(
			@RequestParam(required = false) List<String> ids,
			@RequestParam(required = false) String domainId,
			@ParameterObject Pageable pageRequest) throws Exception {
		if(ids != null) {
			List<LearningScenario> list = learningScenarioRepository.findByIdIn(ids);
			return new PageImpl<>(list);
		} else if(domainId != null) {
			securityHelper.checkRole(domainId, Role.domain, Role.educator);
			return learningScenarioRepository.findByDomainId(domainId, pageRequest); 
		}
		return learningScenarioRepository.findAll(pageRequest);
	}
	
	@GetMapping("/api/scenarios/{id}")
	public LearningScenario getOne(@PathVariable String id) throws Exception {
		LearningScenario entity = learningScenarioRepository.findById(id).orElse(null);
		if(entity != null) {
			securityHelper.checkRole(entity.getDomainId(), Role.domain, Role.educator);
		}
		return entity;		
	}
	
	@PostMapping("/api/scenarios")
	public LearningScenario create(@RequestBody LearningScenario learningScenario) throws Exception {
		securityHelper.checkRole(learningScenario.getDomainId(), Role.domain);
		return learningScenarioRepository.save(learningScenario);
	}
	
	@PutMapping("/api/scenarios/{id}")
	public LearningScenario update(@PathVariable String id,
			@RequestBody LearningScenario learningScenario) throws Exception {
		securityHelper.checkRole(learningScenario.getDomainId(), Role.domain);
		LearningScenario ls = learningScenarioRepository.findById(id).orElse(null);
		if(ls == null) {
			throw new EntityException("entity not found");
		}
		if(!ls.getDomainId().equals(learningScenario.getDomainId())) {
			throw new UnauthorizedException("role not found");
		}				
		learningScenario.setId(id);
		return dataManager.updateLearningScenario(learningScenario);
	}
	
	@DeleteMapping("/api/scenarios/{id}")
	public LearningScenario delete(@PathVariable String id) throws Exception {
		LearningScenario learningScenario = learningScenarioRepository.findById(id).orElse(null);
		if(learningScenario != null) {
			securityHelper.checkRole(learningScenario.getDomainId(), Role.domain);
			learningScenarioRepository.deleteById(id);
		}
		return learningScenario;
	}

}

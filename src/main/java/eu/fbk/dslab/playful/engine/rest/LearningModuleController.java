package eu.fbk.dslab.playful.engine.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.fbk.dslab.playful.engine.exception.UnauthorizedException;
import eu.fbk.dslab.playful.engine.manager.DataManager;
import eu.fbk.dslab.playful.engine.manager.EntityManager;
import eu.fbk.dslab.playful.engine.model.LearningModule;
import eu.fbk.dslab.playful.engine.model.LearningScenario;
import eu.fbk.dslab.playful.engine.repository.LearningModuleRepository;
import eu.fbk.dslab.playful.engine.repository.LearningScenarioRepository;
import eu.fbk.dslab.playful.engine.security.UserRole.Role;

@RestController
public class LearningModuleController extends PlayfulController {
	@Autowired
	LearningModuleRepository learningModuleRepository;
	@Autowired
	LearningScenarioRepository learningScenarioRepository;
	
	@Autowired
	DataManager dataManager;
	
	@Autowired
	EntityManager entityManager;
	
	@GetMapping("/api/modules")
	public Page<LearningModule> getList(
			@RequestParam(required = false) List<String> ids,
			@RequestParam(required = false) String learningScenarioId) throws Exception {
		List<LearningModule> list = new ArrayList<>();
		if(ids != null) {
			list = learningModuleRepository.findByIdIn(ids);
		} else if(learningScenarioId != null) {
			LearningScenario ls = learningScenarioRepository.findById(learningScenarioId).orElse(null);
			if(ls != null) {
				if(securityHelper.hasRole(ls.getDomainId(), Role.domain)) {
					list = learningModuleRepository.findByLearningScenarioId(learningScenarioId, Sort.by(Direction.ASC, "position"));
				} else if(entityManager.checkEducator(learningScenarioId)) {
					list = learningModuleRepository.findByLearningScenarioId(learningScenarioId, Sort.by(Direction.ASC, "position"));	
				}
			}
		}
		return new PageImpl<>(list);
	}
	
	@GetMapping("/api/modules/{id}")
	public LearningModule getOne(@PathVariable String id) throws Exception {
		LearningModule entity = learningModuleRepository.findById(id).orElse(null);
		if(entity != null) {
			if(!securityHelper.hasRole(entity.getDomainId(), Role.domain) && 
					!entityManager.checkEducator(entity.getLearningScenarioId()))
				throw new UnauthorizedException("role not found");
		}
		return entity;
	}
	
	@PostMapping("/api/modules")
	public LearningModule create(@RequestBody LearningModule learningModule) throws Exception {
		if(securityHelper.hasRole(learningModule.getDomainId(), Role.domain) || 
				entityManager.checkEducator(learningModule.getLearningScenarioId())) {
			return learningModuleRepository.save(learningModule);
		}
		throw new UnauthorizedException("role not found");
	}
	
	@PutMapping("/api/modules/{id}")
	public LearningModule update(@PathVariable String id,
			@RequestBody LearningModule learningModule) throws Exception {
		learningModule.setId(id);
		if(securityHelper.hasRole(learningModule.getDomainId(), Role.domain) || 
				entityManager.checkEducator(learningModule.getLearningScenarioId())) {
			return dataManager.updateLearningModule(learningModule);
		}
		throw new UnauthorizedException("role not found");
	}
	
	@DeleteMapping("/api/modules/{id}")
	public LearningModule delete(@PathVariable String id) throws Exception {
		LearningModule learningModule = learningModuleRepository.findById(id).orElse(null);
		if(learningModule != null) {
			if(securityHelper.hasRole(learningModule.getDomainId(), Role.domain) || 
					entityManager.checkEducator(learningModule.getLearningScenarioId())) {
				dataManager.removeLearningModule(learningModule);
			}
		}
		return learningModule;
	}

}

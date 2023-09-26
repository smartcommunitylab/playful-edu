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
import eu.fbk.dslab.playful.engine.manager.EntityManager;
import eu.fbk.dslab.playful.engine.model.LearningFragment;
import eu.fbk.dslab.playful.engine.model.LearningModule;
import eu.fbk.dslab.playful.engine.repository.LearningFragmentRepository;
import eu.fbk.dslab.playful.engine.repository.LearningModuleRepository;
import eu.fbk.dslab.playful.engine.security.UserRole.Role;

@RestController
public class LearningFragmentController extends PlayfulController {
	@Autowired
	LearningModuleRepository learningModuleRepository;
	@Autowired
	LearningFragmentRepository learningFragmentRepository;
	
	@Autowired
	EntityManager entityManager;
	
	@GetMapping("/api/fragments")
	public Page<LearningFragment> getList(
			@RequestParam(required = false) List<String> ids,
			@RequestParam(required = false) String learningModuleId) throws Exception {
		List<LearningFragment> list = new ArrayList<>();
		if(ids != null) {
			list = learningFragmentRepository.findByIdIn(ids);
		} else if(learningModuleId != null) {
			LearningModule lm = learningModuleRepository.findById(learningModuleId).orElse(null);
			if(lm != null) {
				if(securityHelper.hasRole(lm.getDomainId(), Role.domain) ||
						entityManager.checkEducator(lm.getLearningScenarioId())) {
					list = learningFragmentRepository.findByLearningModuleId(learningModuleId, Sort.by(Direction.ASC, "position"));
				}
			}
		} 
		return new PageImpl<>(list);
	}
	
	@GetMapping("/api/fragments/{id}")
	public LearningFragment getOne(@PathVariable String id) throws Exception {
		LearningFragment entity = learningFragmentRepository.findById(id).orElse(null);
		if(entity != null) {
			if(securityHelper.hasRole(entity.getDomainId(), Role.domain) || 
					entityManager.checkEducatorModule(entity.getLearningModuleId())) 
				return entity;
			throw new UnauthorizedException("role not found");
		}
		return entity;		
	}
	
	@PostMapping("/api/fragments")
	public LearningFragment create(@RequestBody LearningFragment learningFragment) throws Exception {
		if(securityHelper.hasRole(learningFragment.getDomainId(), Role.domain) || 
				entityManager.checkEducatorModule(learningFragment.getLearningModuleId())) {
			return learningFragmentRepository.save(learningFragment);
		}			
		throw new UnauthorizedException("role not found");
	}
	
	@PutMapping("/api/fragments/{id}")
	public LearningFragment update(@PathVariable String id,
			@RequestBody LearningFragment learningFragment) throws Exception {
		learningFragment.setId(id);
		if(securityHelper.hasRole(learningFragment.getDomainId(), Role.domain) || 
				entityManager.checkEducatorModule(learningFragment.getLearningModuleId())) {
			return learningFragmentRepository.save(learningFragment);
		}		
		throw new UnauthorizedException("role not found");
	}
	
	@DeleteMapping("/api/fragments/{id}")
	public LearningFragment delete(@PathVariable String id) throws Exception {
		LearningFragment learningFragment = learningFragmentRepository.findById(id).orElse(null);
		if(learningFragment != null) {
			LearningModule lm = learningModuleRepository.findById(learningFragment.getLearningModuleId()).orElse(null);
			if(lm != null) {
				if(securityHelper.hasRole(lm.getDomainId(), Role.domain) || 
						entityManager.checkEducator(lm.getLearningScenarioId())) {
					learningFragmentRepository.deleteById(id);
				}			
			}
		}
		return learningFragment;
	}

}

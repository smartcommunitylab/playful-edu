package eu.fbk.dslab.playful.engine.rest;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.fbk.dslab.playful.engine.model.LearningModule;
import eu.fbk.dslab.playful.engine.repository.LearningModuleRepository;

@RestController
public class LearningModuleController {
	@Autowired
	LearningModuleRepository learningModuleRepository;
	
	@GetMapping("/api/modules")
	public Page<LearningModule> getList(
			@RequestParam(required = false) String domainId,
			@RequestParam(required = false) String learningModuleId,
			@ParameterObject Pageable pageRequest) {
		if(learningModuleId != null) {
			return learningModuleRepository.findByLearningScenarioId(learningModuleId, pageRequest);
		}
		if(domainId != null) {
			return learningModuleRepository.findByDomainId(domainId, pageRequest); 
		}
		return learningModuleRepository.findAll(pageRequest);
	}
	
	@GetMapping("/api/modules/{id}")
	public LearningModule getOne(@PathVariable String id) {
		return learningModuleRepository.findById(id).orElse(null);
	}
	
	@GetMapping("/api/modules/many")
	public List<LearningModule> getMany(@RequestParam String[] ids) {
		return learningModuleRepository.findByIdIsIn(ids);
	}
	
	@PostMapping("/api/modules")
	public LearningModule create(@RequestBody LearningModule lLearningModule) {
		return learningModuleRepository.save(lLearningModule);
	}
	
	@PutMapping("/api/modules/{id}")
	public LearningModule update(@PathVariable String id,
			@RequestBody LearningModule lLearningModule) {
		lLearningModule.setId(id);
		return learningModuleRepository.save(lLearningModule);
	}
	
	@DeleteMapping("/api/modules/{id}")
	public LearningModule delete(@PathVariable String id) {
		LearningModule learningModule = learningModuleRepository.findById(id).orElse(null);
		if(learningModule != null) {
			learningModuleRepository.deleteById(id);
		}
		return learningModule;
	}

}

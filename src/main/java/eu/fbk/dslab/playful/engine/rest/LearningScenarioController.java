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

import eu.fbk.dslab.playful.engine.model.LearningScenario;
import eu.fbk.dslab.playful.engine.repository.LearningScenarioRepository;

@RestController
public class LearningScenarioController {
	@Autowired
	LearningScenarioRepository learningScenarioRepository;
	
	@GetMapping("/api/scenarios")
	public Page<LearningScenario> getList(
			@RequestParam(required = false) String domainId,
			@ParameterObject Pageable pageRequest) {
		if(domainId != null) {
			return learningScenarioRepository.findByDomainId(domainId, pageRequest); 
		}
		return learningScenarioRepository.findAll(pageRequest);
	}
	
	@GetMapping("/api/scenarios/{id}")
	public LearningScenario getOne(@PathVariable String id) {
		return learningScenarioRepository.findById(id).orElse(null);
	}
	
	@GetMapping("/api/scenarios/many")
	public List<LearningScenario> getMany(@RequestParam List<String> ids) {
		return learningScenarioRepository.findByIdIn(ids);
	}
	
	@PostMapping("/api/scenarios")
	public LearningScenario create(@RequestBody LearningScenario learningScenario) {
		return learningScenarioRepository.save(learningScenario);
	}
	
	@PutMapping("/api/scenarios/{id}")
	public LearningScenario update(@PathVariable String id,
			@RequestBody LearningScenario learningScenario) {
		learningScenario.setId(id);
		return learningScenarioRepository.save(learningScenario);
	}
	
	@DeleteMapping("/api/scenarios/{id}")
	public LearningScenario delete(@PathVariable String id) {
		LearningScenario learningScenario = learningScenarioRepository.findById(id).orElse(null);
		if(learningScenario != null) {
			learningScenarioRepository.deleteById(id);
		}
		return learningScenario;
	}

}

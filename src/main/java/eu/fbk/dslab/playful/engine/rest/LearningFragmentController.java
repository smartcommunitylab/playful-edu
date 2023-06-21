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

import eu.fbk.dslab.playful.engine.model.LearningFragment;
import eu.fbk.dslab.playful.engine.repository.LearningFragmentRepository;

@RestController
public class LearningFragmentController {
	@Autowired
	LearningFragmentRepository learningFragmentRepository;
	
	@GetMapping("/api/fragments")
	public Page<LearningFragment> getList(
			@RequestParam(required = false) String domainId,
			@RequestParam(required = false) String learningModuleId,
			@ParameterObject Pageable pageRequest) {
		if(learningModuleId != null) {
			return learningFragmentRepository.findByLearningModuleId(learningModuleId, pageRequest);
		}
		if(domainId != null) {
			return learningFragmentRepository.findByDomainId(domainId, pageRequest); 
		}
		return learningFragmentRepository.findAll(pageRequest);
	}
	
	@GetMapping("/api/fragments/{id}")
	public LearningFragment getOne(@PathVariable String id) {
		return learningFragmentRepository.findById(id).orElse(null);
	}
	
	@GetMapping("/api/fragments/many")
	public List<LearningFragment> getMany(@RequestParam List<String> ids) {
		return learningFragmentRepository.findByIdIn(ids);
	}
	
	@PostMapping("/api/fragments")
	public LearningFragment create(@RequestBody LearningFragment lLearningFragment) {
		return learningFragmentRepository.save(lLearningFragment);
	}
	
	@PutMapping("/api/fragments/{id}")
	public LearningFragment update(@PathVariable String id,
			@RequestBody LearningFragment lLearningFragment) {
		lLearningFragment.setId(id);
		return learningFragmentRepository.save(lLearningFragment);
	}
	
	@DeleteMapping("/api/fragments/{id}")
	public LearningFragment delete(@PathVariable String id) {
		LearningFragment learningFragment = learningFragmentRepository.findById(id).orElse(null);
		if(learningFragment != null) {
			learningFragmentRepository.deleteById(id);
		}
		return learningFragment;
	}

}

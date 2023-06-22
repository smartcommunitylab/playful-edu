package eu.fbk.dslab.playful.engine.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
			@RequestParam(required = false) List<String> ids,
			@RequestParam(required = false) String learningModuleId) {
		List<LearningFragment> list = null;
		if(ids != null) {
			list = learningFragmentRepository.findByIdIn(ids);
		} else if(learningModuleId != null) {
			list = learningFragmentRepository.findByLearningModuleId(learningModuleId);
		} else {
			list = learningFragmentRepository.findAll(); 
		}
		return new PageImpl<>(list);
	}
	
	@GetMapping("/api/fragments/{id}")
	public LearningFragment getOne(@PathVariable String id) {
		return learningFragmentRepository.findById(id).orElse(null);
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

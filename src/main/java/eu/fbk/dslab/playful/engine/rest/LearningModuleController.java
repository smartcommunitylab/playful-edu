package eu.fbk.dslab.playful.engine.rest;

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

import eu.fbk.dslab.playful.engine.model.LearningModule;
import eu.fbk.dslab.playful.engine.repository.LearningModuleRepository;

@RestController
public class LearningModuleController {
	@Autowired
	LearningModuleRepository learningModuleRepository;
	
	@GetMapping("/api/modules")
	public Page<LearningModule> getList(
			@RequestParam(required = false) List<String> ids,
			@RequestParam(required = false) String learningModuleId) {
		List<LearningModule> list = null;
		if(ids != null) {
			list = learningModuleRepository.findByIdIn(ids);
		} else if(learningModuleId != null) {
			list = learningModuleRepository.findByLearningScenarioId(learningModuleId, Sort.by(Direction.ASC, "position"));
		} else {
			list = learningModuleRepository.findAll();
		}
		return new PageImpl<>(list);
	}
	
	@GetMapping("/api/modules/{id}")
	public LearningModule getOne(@PathVariable String id) {
		return learningModuleRepository.findById(id).orElse(null);
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

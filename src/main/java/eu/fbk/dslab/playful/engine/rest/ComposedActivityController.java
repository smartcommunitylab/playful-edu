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

import eu.fbk.dslab.playful.engine.model.ComposedActivity;
import eu.fbk.dslab.playful.engine.repository.ComposedActivityRepository;

@RestController
public class ComposedActivityController {
	@Autowired
	ComposedActivityRepository composedActivityRepository;
	
	@GetMapping("/api/composed-activities")
	public Page<ComposedActivity> getList(
			@RequestParam(required = false) List<String> ids,
			@RequestParam(required = false) String learningFragmentId) {
		List<ComposedActivity> list = null;
		if(ids != null) {
			list = composedActivityRepository.findByIdIn(ids);
		} else if(learningFragmentId != null) {
			list = composedActivityRepository.findByLearningFragmentId(learningFragmentId);
		} else {
			list = composedActivityRepository.findAll(); 
		}
		return new PageImpl<>(list);
	}
	
	@GetMapping("/api/composed-activities/{id}")
	public ComposedActivity getOne(@PathVariable String id) {
		return composedActivityRepository.findById(id).orElse(null);
	}
	
	@PostMapping("/api/composed-activities")
	public ComposedActivity create(@RequestBody ComposedActivity activity) {
		return composedActivityRepository.save(activity);
	}
	
	@PutMapping("/api/composed-activities/{id}")
	public ComposedActivity update(@PathVariable String id,
			@RequestBody ComposedActivity activity) {
		activity.setId(id);
		return composedActivityRepository.save(activity);
	}
	
	@DeleteMapping("/api/composed-activities/{id}")
	public ComposedActivity delete(@PathVariable String id) {
		ComposedActivity activity = composedActivityRepository.findById(id).orElse(null);
		if(activity != null) {
			composedActivityRepository.deleteById(id);
		}
		return activity;
	}

}

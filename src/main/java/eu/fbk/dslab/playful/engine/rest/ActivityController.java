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

import eu.fbk.dslab.playful.engine.model.Activity;
import eu.fbk.dslab.playful.engine.repository.ActivityRepository;

@RestController
public class ActivityController {
	@Autowired
	ActivityRepository activityRepository;
	
	@GetMapping("/api/activities")
	public Page<Activity> getList(
			@RequestParam(required = false) List<String> ids,
			@RequestParam(required = false) String learningFragmentId) {
		List<Activity> list = null;
		if(ids != null) {
			list = activityRepository.findByIdIn(ids);
		} else if(learningFragmentId != null) {
			list = activityRepository.findByLearningFragmentId(learningFragmentId);
		} else {
			list = activityRepository.findAll();
		}
		return new PageImpl<>(list);
	}
	
	@GetMapping("/api/activities/{id}")
	public Activity getOne(@PathVariable String id) {
		return activityRepository.findById(id).orElse(null);
	}
	
	@PostMapping("/api/activities")
	public Activity create(@RequestBody Activity activity) {
		return activityRepository.save(activity);
	}
	
	@PutMapping("/api/activities/{id}")
	public Activity update(@PathVariable String id,
			@RequestBody Activity activity) {
		activity.setId(id);
		return activityRepository.save(activity);
	}
	
	@DeleteMapping("/api/activities/{id}")
	public Activity delete(@PathVariable String id) {
		Activity activity = activityRepository.findById(id).orElse(null);
		if(activity != null) {
			activityRepository.deleteById(id);
		}
		return activity;
	}

}

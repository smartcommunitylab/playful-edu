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

import eu.fbk.dslab.playful.engine.model.Activity;
import eu.fbk.dslab.playful.engine.repository.ActivityRepository;

@RestController
public class ActivityController {
	@Autowired
	ActivityRepository activityRepository;
	
	@GetMapping("/api/activities")
	public Page<Activity> getList(
			@RequestParam(required = false) String domainId,
			@RequestParam(required = false) String composedActivityId,
			@ParameterObject Pageable pageRequest) {
		if(composedActivityId != null) {
			return activityRepository.findByComposedActivityId(composedActivityId, pageRequest);
		}
		if(domainId != null) {
			return activityRepository.findByDomainId(domainId, pageRequest);	
		}
		return activityRepository.findAll(pageRequest);
	}
	
	@GetMapping("/api/activities/{id}")
	public Activity getOne(@PathVariable String id) {
		return activityRepository.findById(id).orElse(null);
	}
	
	@GetMapping("/api/activities/many")
	public List<Activity> getMany(@RequestParam String[] ids) {
		return activityRepository.findByIdIsIn(ids);
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

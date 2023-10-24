package eu.fbk.dslab.playful.engine.rest;

import java.util.ArrayList;
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

import eu.fbk.dslab.playful.engine.exception.UnauthorizedException;
import eu.fbk.dslab.playful.engine.manager.DataManager;
import eu.fbk.dslab.playful.engine.manager.EntityManager;
import eu.fbk.dslab.playful.engine.model.Activity;
import eu.fbk.dslab.playful.engine.model.LearningFragment;
import eu.fbk.dslab.playful.engine.repository.ActivityRepository;
import eu.fbk.dslab.playful.engine.repository.LearningFragmentRepository;
import eu.fbk.dslab.playful.engine.security.UserRole.Role;

@RestController
public class ActivityController extends PlayfulController {
	@Autowired
	ActivityRepository activityRepository;
	@Autowired
	LearningFragmentRepository learningFragmentRepository;

	@Autowired
	DataManager dataManager;

	@Autowired
	EntityManager entityManager;
	
	@GetMapping("/api/activities")
	public Page<Activity> getList(
			@RequestParam(required = false) List<String> ids,
			@RequestParam(required = false) String learningFragmentId) throws Exception {
		List<Activity> list = new ArrayList<>();
		if(ids != null) {
			list = activityRepository.findByIdIn(ids);
		} else if(learningFragmentId != null) {
			LearningFragment lf = learningFragmentRepository.findById(learningFragmentId).orElse(null);
			if(lf != null) {
				if(securityHelper.hasRole(lf.getDomainId(), Role.domain) ||
						entityManager.checkEducatorFragment(learningFragmentId)) {
					list = activityRepository.findByLearningFragmentId(learningFragmentId);
				}
			}
		} 
		return new PageImpl<>(list);
	}
	
	@GetMapping("/api/activities/{id}")
	public Activity getOne(@PathVariable String id) throws Exception {
		Activity entity = activityRepository.findById(id).orElse(null);
		if(entity != null) {
			if(securityHelper.hasRole(entity.getDomainId(), Role.domain) ||
					entityManager.checkEducatorFragment(entity.getLearningFragmentId())) {
				return entity;
			}
			throw new UnauthorizedException("role not found");
		}
		return entity;
	}
	
	@PostMapping("/api/activities")
	public Activity create(@RequestBody Activity activity) throws Exception {
		if(securityHelper.hasRole(activity.getDomainId(), Role.domain) ||
				entityManager.checkEducatorFragment(activity.getLearningFragmentId())) {
			return activityRepository.save(activity);
		}
		throw new UnauthorizedException("role not found");
	}
	
	@PutMapping("/api/activities/{id}")
	public Activity update(@PathVariable String id,
			@RequestBody Activity activity) throws Exception {
		activity.setId(id);
		if(securityHelper.hasRole(activity.getDomainId(), Role.domain) ||
				entityManager.checkEducatorFragment(activity.getLearningFragmentId())) {
			return dataManager.updateActivity(activity);
		}
		throw new UnauthorizedException("role not found");
	}
	
	@DeleteMapping("/api/activities/{id}")
	public Activity delete(@PathVariable String id) throws Exception {
		Activity activity = activityRepository.findById(id).orElse(null);
		if(activity != null) {
			if(securityHelper.hasRole(activity.getDomainId(), Role.domain) ||
					entityManager.checkEducatorFragment(activity.getLearningFragmentId())) {			
				dataManager.removeActivity(activity);
			}
		}
		return activity;
	}

}

package eu.fbk.dslab.playful.engine.rest;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.fbk.dslab.playful.engine.model.ExternalActivity;
import eu.fbk.dslab.playful.engine.repository.ExternalActivityRepository;

@RestController
public class ExternalActivityController {
	@Autowired
	ExternalActivityRepository externalActivityRepository;
	
	@GetMapping("/api/externalActivities")
	public Page<ExternalActivity> getList(
			@RequestParam(required = false) List<String> ids,
			@RequestParam(required = false) String domainId,
			@ParameterObject Pageable pageRequest) {
		if(ids != null) {
			List<ExternalActivity> list = externalActivityRepository.findByIdIn(ids);
			return new PageImpl<>(list);
		} else if(domainId != null) {
			return externalActivityRepository.findByDomainId(domainId, pageRequest); 
		}
		return externalActivityRepository.findAll(pageRequest);
	}
	
	@GetMapping("/api/externalActivities/{id}")
	public ExternalActivity getOne(@PathVariable String id) {
		return externalActivityRepository.findById(id).orElse(null);
	}
	
	@PostMapping("/api/externalActivities")
	public ExternalActivity create(@RequestBody ExternalActivity externalActivity) {
		return externalActivityRepository.save(externalActivity);
	}
	
	@PutMapping("/api/externalActivities/{id}")
	public ExternalActivity update(@PathVariable String id,
			@RequestBody ExternalActivity externalActivity) {
		externalActivity.setId(id);
		return externalActivityRepository.save(externalActivity);
	}
	
	@DeleteMapping("/api/externalActivities/{id}")
	public ExternalActivity delete(@PathVariable String id) {
		ExternalActivity externalActivity = externalActivityRepository.findById(id).orElse(null);
		if(externalActivity != null) {
			externalActivityRepository.deleteById(id);
		}
		return externalActivity;
	}

}

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

import eu.fbk.dslab.playful.engine.exception.EntityException;
import eu.fbk.dslab.playful.engine.exception.UnauthorizedException;
import eu.fbk.dslab.playful.engine.model.ExternalActivity;
import eu.fbk.dslab.playful.engine.repository.ExternalActivityRepository;
import eu.fbk.dslab.playful.engine.security.UserRole.Role;

@RestController
public class ExternalActivityController extends PlayfulController {
	@Autowired
	ExternalActivityRepository externalActivityRepository;
	
	@GetMapping("/api/external-activities")
	public Page<ExternalActivity> getList(
			@RequestParam(required = false) List<String> ids,
			@RequestParam(required = false) String domainId,
			@ParameterObject Pageable pageRequest) throws Exception {
		if(ids != null) {
			List<ExternalActivity> list = externalActivityRepository.findByIdIn(ids);
			return new PageImpl<>(list);
		} else if(domainId != null) {
			securityHelper.checkRole(domainId, Role.domain, Role.educator);
			return externalActivityRepository.findByDomainId(domainId, pageRequest); 
		}
		return externalActivityRepository.findAll(pageRequest);
	}
	
	@GetMapping("/api/external-activities/{id}")
	public ExternalActivity getOne(@PathVariable String id) throws Exception {
		ExternalActivity entity = externalActivityRepository.findById(id).orElse(null);
		if(entity != null) {
			securityHelper.checkRole(entity.getDomainId(), Role.domain, Role.educator);
		}
		return entity;
	}
	
	@PostMapping("/api/external-activities")
	public ExternalActivity create(@RequestBody ExternalActivity externalActivity) throws Exception {
		securityHelper.checkRole(externalActivity.getDomainId(), Role.domain);
		return externalActivityRepository.save(externalActivity);
	}
	
	@PutMapping("/api/external-activities/{id}")
	public ExternalActivity update(@PathVariable String id,
			@RequestBody ExternalActivity externalActivity) throws Exception {
		securityHelper.checkRole(externalActivity.getDomainId(), Role.domain);
		ExternalActivity ea = externalActivityRepository.findById(id).orElse(null);
		if(ea == null) {
			throw new EntityException("entity not found");
		}
		if(!ea.getDomainId().equals(externalActivity.getDomainId())) {
			throw new UnauthorizedException("role not found");
		}		
		externalActivity.setId(id);
		return externalActivityRepository.save(externalActivity);
	}
	
	@DeleteMapping("/api/external-activities/{id}")
	public ExternalActivity delete(@PathVariable String id) throws Exception {
		ExternalActivity externalActivity = externalActivityRepository.findById(id).orElse(null);
		if(externalActivity != null) {
			securityHelper.checkRole(externalActivity.getDomainId(), Role.domain);
			externalActivityRepository.deleteById(id);
		}
		return externalActivity;
	}

}

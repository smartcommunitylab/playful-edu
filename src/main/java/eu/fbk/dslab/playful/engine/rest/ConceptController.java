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
import eu.fbk.dslab.playful.engine.manager.DataManager;
import eu.fbk.dslab.playful.engine.model.Concept;
import eu.fbk.dslab.playful.engine.repository.ConceptRepository;
import eu.fbk.dslab.playful.engine.security.UserRole.Role;

@RestController
public class ConceptController extends PlayfulController {
	@Autowired
	ConceptRepository conceptRepository;
	
	@Autowired
	DataManager dataManager;
	
	@GetMapping("/api/concepts")
	public Page<Concept> getList(
			@RequestParam(required = false) List<String> ids,
			@RequestParam(required = false) String domainId,
			@ParameterObject Pageable pageRequest) throws Exception {
		if(ids != null) {
			List<Concept> list = conceptRepository.findByIdIn(ids);
			return new PageImpl<>(list);
		} else if(domainId != null) {
			securityHelper.checkRole(domainId, Role.domain, Role.educator);
			return conceptRepository.findByDomainId(domainId, pageRequest);	
		}
		return conceptRepository.findAll(pageRequest);
	}
	
	@GetMapping("/api/concepts/{id}")
	public Concept getOne(@PathVariable String id) throws Exception {
		Concept entity = conceptRepository.findById(id).orElse(null);
		if(entity != null) {
			securityHelper.checkRole(entity.getDomainId(), Role.domain, Role.educator);
		}
		return entity;
	}
	
	@PostMapping("/api/concepts")
	public Concept create(@RequestBody Concept concept) throws Exception {
		securityHelper.checkRole(concept.getDomainId(), Role.domain);
		return conceptRepository.save(concept);
	}
	
	@PutMapping("/api/concepts/{id}")
	public Concept update(@PathVariable String id,
			@RequestBody Concept concept) throws Exception {
		securityHelper.checkRole(concept.getDomainId(), Role.domain);
		Concept c = conceptRepository.findById(id).orElse(null);
		if(c == null) {
			throw new EntityException("entity not found");
		}
		if(!c.getDomainId().equals(concept.getDomainId())) {
			throw new UnauthorizedException("role not found");
		}		
		concept.setId(id);
		return conceptRepository.save(concept);
	}
	
	@DeleteMapping("/api/concepts/{id}")
	public Concept delete(@PathVariable String id) throws Exception {
		Concept concept = conceptRepository.findById(id).orElse(null);
		if(concept != null) {
			securityHelper.checkRole(concept.getDomainId(), Role.domain);
			return dataManager.removeConcept(id);
		}
		return concept;
	}

}

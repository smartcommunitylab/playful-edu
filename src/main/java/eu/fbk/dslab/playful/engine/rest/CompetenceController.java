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
import eu.fbk.dslab.playful.engine.model.Competence;
import eu.fbk.dslab.playful.engine.repository.CompetenceRepository;
import eu.fbk.dslab.playful.engine.security.UserRole.Role;

@RestController
public class CompetenceController extends PlayfulController {
	@Autowired
	CompetenceRepository competenceRepository;

	@GetMapping("/api/competences")
	public Page<Competence> getList(
			@RequestParam(required = false) List<String> ids,
			@RequestParam(required = false) String domainId,
			@ParameterObject Pageable pageRequest) throws Exception {
		if(ids != null) {
			List<Competence> list = competenceRepository.findByIdIn(ids);
			return new PageImpl<>(list);
		} else if(domainId != null) {
			securityHelper.checkRole(domainId, Role.domain, Role.educator);
			return competenceRepository.findByDomainId(domainId, pageRequest); 
		}
		return competenceRepository.findAll(pageRequest);
	}
	
	@GetMapping("/api/competences/{id}")
	public Competence getOne(@PathVariable String id) throws Exception {
		Competence entity = competenceRepository.findById(id).orElse(null);
		if(entity != null) {
			securityHelper.checkRole(entity.getDomainId(), Role.domain, Role.educator);
		}
		return entity;
	}
	
	@PostMapping("/api/competences")
	public Competence create(@RequestBody Competence competence) throws Exception {
		securityHelper.checkRole(competence.getDomainId(), Role.domain);
		return competenceRepository.save(competence);
	}
	
	@PutMapping("/api/competences/{id}")
	public Competence update(@PathVariable String id,
			@RequestBody Competence competence) throws Exception {
		securityHelper.checkRole(competence.getDomainId(), Role.domain);
		Competence c = competenceRepository.findById(id).orElse(null);
		if(c == null) {
			throw new EntityException("entity not found");
		}
		if(!c.getDomainId().equals(competence.getDomainId())) {
			throw new UnauthorizedException("role not found");
		}				
		competence.setId(id);
		return competenceRepository.save(competence);
	}
	
	@DeleteMapping("/api/competences/{id}")
	public Competence delete(@PathVariable String id) throws Exception {
		Competence competence = competenceRepository.findById(id).orElse(null);
		if(competence != null) {
			securityHelper.checkRole(competence.getDomainId(), Role.domain);
			competenceRepository.deleteById(id);
		}
		return competence;
	}

}

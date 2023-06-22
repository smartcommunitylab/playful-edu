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

import eu.fbk.dslab.playful.engine.model.Competence;
import eu.fbk.dslab.playful.engine.repository.CompetenceRepository;

@RestController
public class CompetenceController {
	@Autowired
	CompetenceRepository competenceRepository;
	
	@GetMapping("/api/competences")
	public Page<Competence> getList(
			@RequestParam(required = false) List<String> ids,
			@RequestParam(required = false) String domainId,
			@ParameterObject Pageable pageRequest) {
		if(ids != null) {
			List<Competence> list = competenceRepository.findByIdIn(ids);
			return new PageImpl<>(list);
		} else if(domainId != null) {
			return competenceRepository.findByDomainId(domainId, pageRequest); 
		}
		return competenceRepository.findAll(pageRequest);
	}
	
	@GetMapping("/api/competences/{id}")
	public Competence getOne(@PathVariable String id) {
		return competenceRepository.findById(id).orElse(null);
	}
	
	@PostMapping("/api/competences")
	public Competence create(@RequestBody Competence competence) {
		return competenceRepository.save(competence);
	}
	
	@PutMapping("/api/competences/{id}")
	public Competence update(@PathVariable String id,
			@RequestBody Competence competence) {
		competence.setId(id);
		return competenceRepository.save(competence);
	}
	
	@DeleteMapping("/api/competences/{id}")
	public Competence delete(@PathVariable String id) {
		Competence competence = competenceRepository.findById(id).orElse(null);
		if(competence != null) {
			competenceRepository.deleteById(id);
		}
		return competence;
	}

}

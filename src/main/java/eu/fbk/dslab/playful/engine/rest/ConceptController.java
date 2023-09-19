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

import eu.fbk.dslab.playful.engine.manager.DataManger;
import eu.fbk.dslab.playful.engine.model.Concept;
import eu.fbk.dslab.playful.engine.repository.ConceptRepository;

@RestController
public class ConceptController {
	@Autowired
	ConceptRepository conceptRepository;
	@Autowired
	DataManger dataManger;
	
	@GetMapping("/api/concepts")
	public Page<Concept> getList(
			@RequestParam(required = false) List<String> ids,
			@RequestParam(required = false) String domainId,
			@ParameterObject Pageable pageRequest) {
		if(ids != null) {
			List<Concept> list = conceptRepository.findByIdIn(ids);
			return new PageImpl<>(list);
		} else if(domainId != null) {
			return conceptRepository.findByDomainId(domainId, pageRequest);	
		}
		return conceptRepository.findAll(pageRequest);
	}
	
	@GetMapping("/api/concepts/{id}")
	public Concept getOne(@PathVariable String id) {
		return conceptRepository.findById(id).orElse(null);
	}
	
	@PostMapping("/api/concepts")
	public Concept create(@RequestBody Concept concept) {
		return conceptRepository.save(concept);
	}
	
	@PutMapping("/api/concepts/{id}")
	public Concept update(@PathVariable String id,
			@RequestBody Concept concept) {
		concept.setId(id);
		return conceptRepository.save(concept);
	}
	
	@DeleteMapping("/api/concepts/{id}")
	public Concept delete(@PathVariable String id) {
		return dataManger.removeConcept(id);
	}

}

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

import eu.fbk.dslab.playful.engine.model.Educator;
import eu.fbk.dslab.playful.engine.repository.EducatorRepository;

@RestController
public class EducatorController {
	@Autowired
	EducatorRepository educatorRepository;
	
	@GetMapping("/api/educators")
	public Page<Educator> getList(
			@RequestParam(required = false) String domainId,
			@ParameterObject Pageable pageRequest) {
		if(domainId != null) {
			return educatorRepository.findByDomainId(domainId, pageRequest); 
		}
		return educatorRepository.findAll(pageRequest);
	}
	
	@GetMapping("/api/educators/{id}")
	public Educator getOne(@PathVariable String id) {
		return educatorRepository.findById(id).orElse(null);
	}
	
	@GetMapping("/api/educators/many")
	public List<Educator> getMany(@RequestParam List<String> ids) {
		return educatorRepository.findByIdIn(ids);
	}
	
	@PostMapping("/api/educators")
	public Educator create(@RequestBody Educator educator) {
		return educatorRepository.save(educator);
	}
	
	@PutMapping("/api/educators/{id}")
	public Educator update(@PathVariable String id,
			@RequestBody Educator educator) {
		educator.setId(id);
		return educatorRepository.save(educator);
	}
	
	@DeleteMapping("/api/educators/{id}")
	public Educator delete(@PathVariable String id) {
		Educator educator = educatorRepository.findById(id).orElse(null);
		if(educator != null) {
			educatorRepository.deleteById(id);
		}
		return educator;
	}

}

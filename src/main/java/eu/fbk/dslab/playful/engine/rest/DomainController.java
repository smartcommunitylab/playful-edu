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

import eu.fbk.dslab.playful.engine.model.Domain;
import eu.fbk.dslab.playful.engine.repository.DomainRepository;

@RestController
public class DomainController {
	@Autowired
	DomainRepository domainRepository;
	
	@GetMapping("/api/domains")
	public Page<Domain> getList(
			@RequestParam(required = false) List<String> ids,
			@ParameterObject Pageable pageRequest) {
		if(ids != null) {
			List<Domain> list = domainRepository.findByIdIn(ids);
			return new PageImpl<>(list);
		}
		return domainRepository.findAll(pageRequest);
	}
	
	@GetMapping("/api/domains/{id}")
	public Domain getOne(@PathVariable String id) {
		return domainRepository.findById(id).orElse(null);
	}
	
	@PostMapping("/api/domains")
	public Domain create(@RequestBody Domain domain) {
		return domainRepository.save(domain);
	}
	
	@PutMapping("/api/domains/{id}")
	public Domain update(@PathVariable String id,
			@RequestBody Domain domain) {
		domain.setId(id);
		return domainRepository.save(domain);
	}
	
	@DeleteMapping("/api/domains/{id}")
	public Domain delete(@PathVariable String id) {
		Domain domain = domainRepository.findById(id).orElse(null);
		if(domain != null) {
			domainRepository.deleteById(id);
		}
		return domain;
	}

}

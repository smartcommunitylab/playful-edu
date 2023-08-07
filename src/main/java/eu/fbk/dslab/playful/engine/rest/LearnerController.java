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

import eu.fbk.dslab.playful.engine.model.Learner;
import eu.fbk.dslab.playful.engine.repository.LearnerRepository;

@RestController
public class LearnerController {
	@Autowired
	LearnerRepository learnerRepository;
	
	@GetMapping("/api/learners")
	public Page<Learner> getList(
			@RequestParam(required = false) List<String> ids,
			@RequestParam(required = false) String domainId,
			@RequestParam(required = false) String text,
			@ParameterObject Pageable pageRequest) {
		if(ids != null) {
			List<Learner> list = learnerRepository.findByIdIn(ids);
			return new PageImpl<>(list);
		} else if(domainId != null) {
			if(text != null) {
				return learnerRepository.findByDomainIdAndText(domainId, text, pageRequest);
			} else {
				return learnerRepository.findByDomainId(domainId, pageRequest);	
			}
		}
		return learnerRepository.findAll(pageRequest);
	}
	
	@GetMapping("/api/learners/{id}")
	public Learner getOne(@PathVariable String id) {
		return learnerRepository.findById(id).orElse(null);
	}
	
	@PostMapping("/api/learners")
	public Learner create(@RequestBody Learner learner) {
		return learnerRepository.save(learner);
	}
	
	@PutMapping("/api/learners/{id}")
	public Learner update(@PathVariable String id,
			@RequestBody Learner learner) {
		learner.setId(id);
		return learnerRepository.save(learner);
	}
	
	@DeleteMapping("/api/learners/{id}")
	public Learner delete(@PathVariable String id) {
		Learner learner = learnerRepository.findById(id).orElse(null);
		if(learner != null) {
			learnerRepository.deleteById(id);
		}
		return learner;
	}

}

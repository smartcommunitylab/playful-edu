package eu.fbk.dslab.playful.engine.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.fbk.dslab.playful.engine.manager.IntegrationService;
import eu.fbk.dslab.playful.engine.model.Educator;
import eu.fbk.dslab.playful.engine.model.ExternalActivity;
import eu.fbk.dslab.playful.engine.model.Group;
import eu.fbk.dslab.playful.engine.model.Learner;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class IntegrationController {
	@Autowired
	IntegrationService integrationService;
	
	@PostMapping("/api/ext/int/educators")
	public ResponseEntity<Void> importEducators(
			@RequestParam String domainId,
			@RequestBody List<Educator> educators) {
		integrationService.importEducators(domainId, educators);
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/api/ext/int/learners")
	public ResponseEntity<Void> importLearners(
			@RequestParam String domainId,
			@RequestBody List<Learner> learners) {
		integrationService.importLearners(domainId, learners);
		return ResponseEntity.ok(null);		
	}
	
	@PostMapping("/api/ext/int/groups")
	public ResponseEntity<Void> importGroups(
			@RequestParam String domainId,
			@RequestBody List<Group> groups) {
		integrationService.importGroups(domainId, groups);
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/api/ext/int/activities")
	public ResponseEntity<Void> importExtActivities(
			@RequestParam String domainId,
			@RequestBody List<ExternalActivity> activities) {
		integrationService.importExtActivities(domainId, activities);
		return ResponseEntity.ok(null);
	}

}

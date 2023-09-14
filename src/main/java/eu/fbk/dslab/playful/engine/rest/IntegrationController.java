package eu.fbk.dslab.playful.engine.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import eu.fbk.dslab.playful.engine.integration.StandByMeService;

@RestController
public class IntegrationController {

	@Autowired
	StandByMeService standByMeService;
	
	@GetMapping("/api/int/standbyme/educators")
	public ResponseEntity<Void> getEducatorsStandByMe(
			@RequestParam String domainId) {
		try {
			standByMeService.getEducators(domainId);
			return ResponseEntity.ok(null);
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(null, e.getStatusCode());
		}
	}
	
	@GetMapping("/api/int/standbyme/learners")
	public ResponseEntity<Void> getLearnersStandByMe(
			@RequestParam String domainId) {
		try {
			standByMeService.getLearners(domainId);
			return ResponseEntity.ok(null);
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(null, e.getStatusCode());
		}
	}
	
	@GetMapping("/api/int/standbyme/groups")
	public ResponseEntity<Void> getGroupsStandByMe(
			@RequestParam String domainId) {
		try {
			standByMeService.getGroups(domainId);
			return ResponseEntity.ok(null);
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(null, e.getStatusCode());
		}
	}
	
	@GetMapping("/api/int/standbyme/activities")
	public ResponseEntity<Void> getActivitiesStandByMe(
			@RequestParam String domainId) {
		try {
			standByMeService.getActivities(domainId);
			return ResponseEntity.ok(null);
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(null, e.getStatusCode());
		}
	}

}

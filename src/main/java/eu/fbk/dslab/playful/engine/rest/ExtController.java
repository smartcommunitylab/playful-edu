package eu.fbk.dslab.playful.engine.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import eu.fbk.dslab.playful.engine.dto.ComposedActivityRunDto;
import eu.fbk.dslab.playful.engine.dto.LearningScenarioDto;
import eu.fbk.dslab.playful.engine.manager.RunningScenarioService;
import eu.fbk.dslab.playful.engine.manager.ScenarioService;
import eu.fbk.dslab.playful.engine.model.ActivityStatus;
import eu.fbk.dslab.playful.engine.model.ActivityStatus.Status;
import eu.fbk.dslab.playful.engine.model.Educator;
import eu.fbk.dslab.playful.engine.model.Learner;
import eu.fbk.dslab.playful.engine.model.LearningScenarioRun;

@RestController
public class ExtController {
	@Autowired
	RunningScenarioService runningScenarioService;
	
	@Autowired
	ScenarioService scenarioService;
	
	@PutMapping("/api/ext/learningscenario/run")
	public ResponseEntity<Void> runLearningScenario(
			@RequestParam String id) {
		try {
			runningScenarioService.runLearningScenario(id);
			return ResponseEntity.ok(null);
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(null, e.getStatusCode());
		}		
	}
	
	@PutMapping("/api/ext/learningscenario/run/learner")
	public ResponseEntity<Void> runLearnerLearningScenario(
			@RequestParam String domainId,
			@RequestParam String learningScenarioId,
			@RequestParam String learnerId) {
		try {
			runningScenarioService.runLearnerLearningScenario(learningScenarioId, learnerId);
			return ResponseEntity.ok(null);
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(null, e.getStatusCode());
		}
	}
	
	@PutMapping("/api/ext/activitystatus/status")
	public ResponseEntity<Void> changeActivityStatus(
			@RequestParam String domainId,
			@RequestParam String learningScenarioId,
			@RequestParam String learnerId,
			@RequestParam String activityId,
			@RequestParam Status status) {
		try {
			runningScenarioService.changeActivityStatus(domainId, learningScenarioId, learnerId, activityId, status);
			return ResponseEntity.ok(null);
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(null, e.getStatusCode());
		}
	}
	
	@GetMapping("/api/ext/learningscenariorun")
	public ResponseEntity<LearningScenarioRun> getLearningScenarioRun(
			@RequestParam String domainId,
			@RequestParam String learningScenarioId, 
			@RequestParam String learnerId) {
		try {
			LearningScenarioRun dto = runningScenarioService.getLearningScenarioRun(domainId, learningScenarioId, learnerId);
			return ResponseEntity.ok(dto);
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(null, e.getStatusCode());
		}		
	}
	
	@GetMapping("/api/ext/composedactivity/next")
	public ResponseEntity<ComposedActivityRunDto> getNextActivity(
			@RequestParam String domainId,
			@RequestParam String learningScenarioId, 
			@RequestParam String learnerId) {
		try {
			ComposedActivityRunDto dto = runningScenarioService.getNextActivity(domainId, learningScenarioId, learnerId);
			return ResponseEntity.ok(dto);
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(null, e.getStatusCode());
		}
	}
	
	@GetMapping("/api/ext/activitystatus")
	public ResponseEntity<List<ActivityStatus>> getActivityStatus(
			@RequestParam List<String> ids) {
		return ResponseEntity.ok(runningScenarioService.getActivityStatus(ids));
	}
	
	@GetMapping("/api/ext/learningscenario/educator")
	public ResponseEntity<List<LearningScenarioDto>> getEducatorLearningScenario(
			@RequestParam String domainId,
			@RequestParam String educatorId) {
		try {
			return ResponseEntity.ok(scenarioService.getEducatorScenario(educatorId, domainId));
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(null, e.getStatusCode());
		}		
	}
	
	@GetMapping("/api/ext/learningscenario/learner")
	public ResponseEntity<List<LearningScenarioDto>> getLearnerLearningScenario(
			@RequestParam String domainId,
			@RequestParam String learnerId) {
		try {
			return ResponseEntity.ok(scenarioService.getLearnerScenario(learnerId, domainId));
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(null, e.getStatusCode());
		}		
	}
	
	@GetMapping("/api/ext/learningscenario/public")
	public ResponseEntity<List<LearningScenarioDto>> getPublicLearningScenario(
			@RequestParam String domainId) {
		try {
			return ResponseEntity.ok(scenarioService.getPublicLearningScenario(domainId));
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(null, e.getStatusCode());
		}				
	}
 	
	@GetMapping("/api/ext/learner")
	public ResponseEntity<Learner> getLearner(
			@RequestParam String domainId,
			@RequestParam String nickname) {
		try {
			return ResponseEntity.ok(scenarioService.getLearner(nickname, domainId));
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(null, e.getStatusCode());
		}		
	}
	
	@GetMapping("/api/ext/educator")
	public ResponseEntity<Educator> getEducator(
			@RequestParam String domainId,
			@RequestParam String nickname) {
		try {
			return ResponseEntity.ok(scenarioService.getEducator(nickname, domainId));
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(null, e.getStatusCode());
		}		
	}

}

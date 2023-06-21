package eu.fbk.dslab.playful.engine.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import eu.fbk.dslab.playful.engine.dto.ComposedActivityRunDto;
import eu.fbk.dslab.playful.engine.manager.RunningScenarioService;
import eu.fbk.dslab.playful.engine.model.ActivityStatus;
import eu.fbk.dslab.playful.engine.model.LearningScenarioRun;

@RestController
public class ExtController {
	@Autowired
	RunningScenarioService runningScenarioService;
	
	@PutMapping("/api/ext/learningscenario/{id}/run")
	public ResponseEntity<Void> runLearningScenario(@PathVariable String id) {
		try {
			runningScenarioService.runLearningScenario(id);
			return ResponseEntity.ok(null);
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(null, e.getStatusCode());
		}		
	}
	
	@PutMapping("/api/ext/activitystatus/{id}/status")
	public ResponseEntity<Void> changeActivityStatus(@PathVariable String id, @RequestParam String status) {
		try {
			runningScenarioService.changeActivityStatus(id, status);
			return ResponseEntity.ok(null);
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(null, e.getStatusCode());
		}
	}
	
	@GetMapping("/api/ext/composedactivity/next")
	public ResponseEntity<ComposedActivityRunDto> getNextActivity(String learningScenarioId, String learnerId) {
		try {
			ComposedActivityRunDto dto = runningScenarioService.getNextActivity(learningScenarioId, learnerId);
			return ResponseEntity.ok(dto);
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(null, e.getStatusCode());
		}
	}
	
	@GetMapping("/api/ext/learningscenariorun")
	public ResponseEntity<LearningScenarioRun> getLearningScenarioRun(String learningScenarioId, String learnerId) {
		try {
			LearningScenarioRun dto = runningScenarioService.getLearningScenarioRun(learningScenarioId, learnerId);
			return ResponseEntity.ok(dto);
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(null, e.getStatusCode());
		}		
	}
	
	@GetMapping("/api/ext/activitystatus")
	public ResponseEntity<List<ActivityStatus>> getActivityStatus(List<String> ids) {
		return ResponseEntity.ok(runningScenarioService.getActivityStatus(ids));
	}

}

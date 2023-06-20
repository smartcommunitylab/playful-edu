package eu.fbk.dslab.playful.engine.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="learning-scenario-run")
public class LearningScenarioRun {
	@Id
	private String id;
	@Indexed
	private String domainId;	
	private Date startingDate;
	private Date lastUpdate;
	private boolean completed;
	private String learningScenarioId;
	private String learnerId;
	private List<LearningModuleRun> modules = new ArrayList<>();
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDomainId() {
		return domainId;
	}
	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}
	public Date getStartingDate() {
		return startingDate;
	}
	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public String getLearningScenarioId() {
		return learningScenarioId;
	}
	public void setLearningScenarioId(String learningScenarioId) {
		this.learningScenarioId = learningScenarioId;
	}
	public String getLearnerId() {
		return learnerId;
	}
	public void setLearnerId(String learnerId) {
		this.learnerId = learnerId;
	}
	public List<LearningModuleRun> getModules() {
		return modules;
	}
	public void setModules(List<LearningModuleRun> modules) {
		this.modules = modules;
	}

}

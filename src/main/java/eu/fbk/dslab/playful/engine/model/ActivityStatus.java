package eu.fbk.dslab.playful.engine.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="activities-status")
public class ActivityStatus {
	public static enum Status {
		assigned, in_progress, completed, failed, cancelled_by_educator, cancelled_by_learner
	};
	
	@Id
	private String id;
	@Indexed
	private String domainId;
	@Indexed
	private String learningScenarioRunId;
	private String learningScenarioId;
	private String learningModuleId;
	private String learningFragmentId;
	private String activityId;
	private String externalActivityId;
	private String learnerId;
	private Status status = Status.assigned;
	private Date lastUpdate;
	
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
	public String getLearningScenarioRunId() {
		return learningScenarioRunId;
	}
	public void setLearningScenarioRunId(String learningScenarioRunId) {
		this.learningScenarioRunId = learningScenarioRunId;
	}
	public String getExternalActivityId() {
		return externalActivityId;
	}
	public void setExternalActivityId(String externalActivityId) {
		this.externalActivityId = externalActivityId;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getLearningScenarioId() {
		return learningScenarioId;
	}
	public void setLearningScenarioId(String learningScenarioId) {
		this.learningScenarioId = learningScenarioId;
	}
	public String getLearningModuleId() {
		return learningModuleId;
	}
	public void setLearningModuleId(String learningModuleId) {
		this.learningModuleId = learningModuleId;
	}
	public String getLearningFragmentId() {
		return learningFragmentId;
	}
	public void setLearningFragmentId(String learningFragmentId) {
		this.learningFragmentId = learningFragmentId;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getLearnerId() {
		return learnerId;
	}
	public void setLearnerId(String learnerId) {
		this.learnerId = learnerId;
	}

}

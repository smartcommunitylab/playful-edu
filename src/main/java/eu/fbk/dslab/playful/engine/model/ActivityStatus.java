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
	private String externalActivityId;
	private Status status;
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

}

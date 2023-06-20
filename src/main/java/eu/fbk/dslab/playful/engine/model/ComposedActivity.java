package eu.fbk.dslab.playful.engine.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="composed-activities")
public class ComposedActivity {
	public static enum Type {
		singleton, set, list
	};
	
	@Id
	private String id;
	@Indexed
	private String domainId;
	@Indexed
	private String learningFragmentId;
	private Type type;
	private List<Activity> activities = new ArrayList<>();
	
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
	public String getLearningFragmentId() {
		return learningFragmentId;
	}
	public void setLearningFragmentId(String learningFragmentId) {
		this.learningFragmentId = learningFragmentId;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public List<Activity> getActivities() {
		return activities;
	}
	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

}

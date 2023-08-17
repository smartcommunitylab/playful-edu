package eu.fbk.dslab.playful.engine.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="activities")
public class Activity {
	public static enum Type {
		concrete, abstr, group
	};
	
	@Id
	private String id;
	@Indexed
	private String domainId;
	@Indexed
	private String learningFragmentId;
	private String title;
	private String desc;
	private Type type;
	private int position;
	private List<String> goals = new ArrayList<>();
	private String externalActivityId;
	private String groupCorrelator;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<String> getGoals() {
		return goals;
	}

	public void setGoals(List<String> goals) {
		this.goals = goals;
	}

	public String getExternalActivityId() {
		return externalActivityId;
	}

	public void setExternalActivityId(String externalActivityId) {
		this.externalActivityId = externalActivityId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getGroupCorrelator() {
		return groupCorrelator;
	}

	public void setGroupCorrelator(String groupCorrelator) {
		this.groupCorrelator = groupCorrelator;
	}

	public String getLearningFragmentId() {
		return learningFragmentId;
	}

	public void setLearningFragmentId(String learningFragmentId) {
		this.learningFragmentId = learningFragmentId;
	}


}

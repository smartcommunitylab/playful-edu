package eu.fbk.dslab.playful.engine.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Transient;

import eu.fbk.dslab.playful.engine.model.LearningFragment.SetCompletionRule;
import eu.fbk.dslab.playful.engine.model.LearningFragment.Type;

public class LearningFragmentRun {
	private String learningFragmentId;
	private Type type;
	private SetCompletionRule setCompletionRule;
	private int minActivities;
	private boolean completed = false;
	private boolean open = false;
	
	private List<String> activityStatusIds = new ArrayList<>();
	
	@Transient
	private List<ActivityStatus> activities = new ArrayList<>();

	
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
	public List<String> getActivityStatusIds() {
		return activityStatusIds;
	}
	public void setActivityStatusIds(List<String> activityStatusIds) {
		this.activityStatusIds = activityStatusIds;
	}
	public List<ActivityStatus> getActivities() {
		return activities;
	}
	public void setActivities(List<ActivityStatus> activities) {
		this.activities = activities;
	}
	public SetCompletionRule getSetCompletionRule() {
		return setCompletionRule;
	}
	public void setSetCompletionRule(SetCompletionRule setCompletionRule) {
		this.setCompletionRule = setCompletionRule;
	}
	public int getMinActivities() {
		return minActivities;
	}
	public void setMinActivities(int minActivities) {
		this.minActivities = minActivities;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
}

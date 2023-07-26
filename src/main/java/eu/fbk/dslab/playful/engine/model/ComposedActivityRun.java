package eu.fbk.dslab.playful.engine.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Transient;

import eu.fbk.dslab.playful.engine.model.ComposedActivity.Type;

public class ComposedActivityRun {
	private String composedActivityId;
	private Type type;
	private List<String> activityStatusIds = new ArrayList<>();
	
	@Transient
	private List<ActivityStatus> activities = new ArrayList<>();
	
	public String getComposedActivityId() {
		return composedActivityId;
	}
	public void setComposedActivityId(String composedActivityId) {
		this.composedActivityId = composedActivityId;
	}
	public List<String> getActivityStatusIds() {
		return activityStatusIds;
	}
	public void setActivityStatusIds(List<String> activityStatusIds) {
		this.activityStatusIds = activityStatusIds;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public List<ActivityStatus> getActivities() {
		return activities;
	}
	public void setActivities(List<ActivityStatus> activities) {
		this.activities = activities;
	}
}

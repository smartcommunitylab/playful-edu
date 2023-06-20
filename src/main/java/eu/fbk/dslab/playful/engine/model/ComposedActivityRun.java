package eu.fbk.dslab.playful.engine.model;

import java.util.ArrayList;
import java.util.List;

import eu.fbk.dslab.playful.engine.model.ComposedActivity.Type;

public class ComposedActivityRun {
	private String composedActivityId;
	private Type type;
	private List<String> ActivityStatusIds = new ArrayList<>();
	
	public String getComposedActivityId() {
		return composedActivityId;
	}
	public void setComposedActivityId(String composedActivityId) {
		this.composedActivityId = composedActivityId;
	}
	public List<String> getActivityStatusIds() {
		return ActivityStatusIds;
	}
	public void setActivityStatusIds(List<String> activityStatusIds) {
		ActivityStatusIds = activityStatusIds;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
}

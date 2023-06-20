package eu.fbk.dslab.playful.engine.model;

import java.util.ArrayList;
import java.util.List;

public class ComposedActivityRun {
	private String composedActivityId;
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
}

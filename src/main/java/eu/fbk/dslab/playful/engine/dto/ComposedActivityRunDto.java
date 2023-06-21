package eu.fbk.dslab.playful.engine.dto;

import java.util.ArrayList;
import java.util.List;

import eu.fbk.dslab.playful.engine.model.ActivityStatus;
import eu.fbk.dslab.playful.engine.model.ComposedActivity.Type;
import eu.fbk.dslab.playful.engine.model.ComposedActivityRun;

public class ComposedActivityRunDto {
	private String composedActivityId;
	private Type type;
	private List<ActivityStatus> activities = new ArrayList<>();
	
	public ComposedActivityRunDto() {}
	
	public ComposedActivityRunDto(ComposedActivityRun activityRun) {
		this.composedActivityId = activityRun.getComposedActivityId();
		this.type = activityRun.getType();
	}
	
	public String getComposedActivityId() {
		return composedActivityId;
	}
	public void setComposedActivityId(String composedActivityId) {
		this.composedActivityId = composedActivityId;
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

package eu.fbk.dslab.playful.engine.dto;

import java.util.ArrayList;
import java.util.List;

import eu.fbk.dslab.playful.engine.model.ComposedActivity;
import eu.fbk.dslab.playful.engine.model.ComposedActivity.Type;

public class ComposedActivityDto {
	private String id;
	private Type type;
	private List<ActivityDto> activities = new ArrayList<>();
	
	public ComposedActivityDto() {}
	
	public ComposedActivityDto(ComposedActivity activity) {
		this.id = activity.getId();
		this.type = activity.getType();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public List<ActivityDto> getActivities() {
		return activities;
	}
	public void setActivities(List<ActivityDto> activities) {
		this.activities = activities;
	}

}

package eu.fbk.dslab.playful.engine.dto;

import java.util.ArrayList;
import java.util.List;

import eu.fbk.dslab.playful.engine.model.LearningFragment;
import eu.fbk.dslab.playful.engine.model.LearningFragment.Type;

public class LearningFragmentDto {
	private String id;
	private String title;
	private String desc;
	private Type type;
	private List<ActivityDto> activities = new ArrayList<>();
	
	public LearningFragmentDto() {}
	
	public LearningFragmentDto(LearningFragment fragment) {
		this.id = fragment.getId();
		this.title = fragment.getTitle();
		this.desc = fragment.getDesc();
		this.type = fragment.getType();
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

package eu.fbk.dslab.playful.engine.dto;

import java.util.ArrayList;
import java.util.List;

import eu.fbk.dslab.playful.engine.model.LearningFragment;

public class LearningFragmentDto {
	private String id;
	private String title;
	private String desc;
	private List<ComposedActivityDto> composedActivities = new ArrayList<>();
	
	public LearningFragmentDto() {}
	
	public LearningFragmentDto(LearningFragment fragment) {
		this.id = fragment.getId();
		this.title = fragment.getTitle();
		this.desc = fragment.getDesc();
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

	public List<ComposedActivityDto> getComposedActivities() {
		return composedActivities;
	}

	public void setComposedActivities(List<ComposedActivityDto> composedActivities) {
		this.composedActivities = composedActivities;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



}

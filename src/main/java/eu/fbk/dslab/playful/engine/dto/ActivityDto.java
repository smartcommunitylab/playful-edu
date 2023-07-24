package eu.fbk.dslab.playful.engine.dto;

import java.util.ArrayList;
import java.util.List;

import eu.fbk.dslab.playful.engine.model.Activity;
import eu.fbk.dslab.playful.engine.model.Activity.Type;
import eu.fbk.dslab.playful.engine.model.Concept;
import eu.fbk.dslab.playful.engine.model.ExternalActivity;

public class ActivityDto {
	private String id;
	private String title;
	private String desc;
	private Type type;
	private List<Concept> goals = new ArrayList<>();
	private ExternalActivity externalActivity;
	
	public ActivityDto() {}
	
	public ActivityDto(Activity activity) {
		this.id = activity.getId();
		this.title = activity.getTitle();
		this.desc = activity.getDesc();
		this.type = activity.getType();
	}

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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<Concept> getGoals() {
		return goals;
	}

	public void setGoals(List<Concept> goals) {
		this.goals = goals;
	}

	public ExternalActivity getExternalActivity() {
		return externalActivity;
	}

	public void setExternalActivity(ExternalActivity externalActivity) {
		this.externalActivity = externalActivity;
	}


}

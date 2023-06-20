package eu.fbk.dslab.playful.engine.model;

import java.util.ArrayList;
import java.util.List;

public class LearningFragmentRun {
	private String learningFragmentId;
	private List<ComposedActivityRun> composedActivities = new ArrayList<>();
	
	public String getLearningFragmentId() {
		return learningFragmentId;
	}
	public void setLearningFragmentId(String learningFragmentId) {
		this.learningFragmentId = learningFragmentId;
	}
	public List<ComposedActivityRun> getComposedActivities() {
		return composedActivities;
	}
	public void setComposedActivities(List<ComposedActivityRun> composedActivities) {
		this.composedActivities = composedActivities;
	}
}

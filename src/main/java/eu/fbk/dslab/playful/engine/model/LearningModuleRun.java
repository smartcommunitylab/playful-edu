package eu.fbk.dslab.playful.engine.model;

import java.util.ArrayList;
import java.util.List;

public class LearningModuleRun {
	private String learningModuleId;
	private List<LearningFragmentRun> fragments = new ArrayList<>();
	
	public String getLearningModuleId() {
		return learningModuleId;
	}
	public void setLearningModuleId(String learningModuleId) {
		this.learningModuleId = learningModuleId;
	}
	public List<LearningFragmentRun> getFragments() {
		return fragments;
	}
	public void setFragments(List<LearningFragmentRun> fragments) {
		this.fragments = fragments;
	}
}

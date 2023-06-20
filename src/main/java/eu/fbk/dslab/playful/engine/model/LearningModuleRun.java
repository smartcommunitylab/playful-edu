package eu.fbk.dslab.playful.engine.model;

public class LearningModuleRun {
	private String learningModuleId;
	private LearningFragmentRun fragment;
	
	public String getLearningModuleId() {
		return learningModuleId;
	}
	public void setLearningModuleId(String learningModuleId) {
		this.learningModuleId = learningModuleId;
	}
	public LearningFragmentRun getFragment() {
		return fragment;
	}
	public void setFragment(LearningFragmentRun fragment) {
		this.fragment = fragment;
	}
}

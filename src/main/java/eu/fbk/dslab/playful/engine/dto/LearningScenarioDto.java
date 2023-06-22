package eu.fbk.dslab.playful.engine.dto;

import java.util.ArrayList;
import java.util.List;

import eu.fbk.dslab.playful.engine.model.Educator;
import eu.fbk.dslab.playful.engine.model.Learner;
import eu.fbk.dslab.playful.engine.model.LearningScenario;

public class LearningScenarioDto {
	private String id;
	private String domainId;	
	private String title;
	private String desc;
	private String language;
	private List<Learner> learners = new ArrayList<>();
	private List<Educator> educators = new ArrayList<>();
	private List<LearningModuleDto> modules = new ArrayList<>();
	
	public LearningScenarioDto() {}
	
	public LearningScenarioDto(LearningScenario scenario) {
		this.id = scenario.getId();
		this.domainId = scenario.getDomainId();
		this.title = scenario.getTitle();
		this.desc = scenario.getDesc();
		this.language = scenario.getLanguage();
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

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<Learner> getLearners() {
		return learners;
	}

	public void setLearners(List<Learner> learners) {
		this.learners = learners;
	}

	public List<Educator> getEducators() {
		return educators;
	}

	public void setEducators(List<Educator> educators) {
		this.educators = educators;
	}

	public List<LearningModuleDto> getModules() {
		return modules;
	}

	public void setModules(List<LearningModuleDto> modules) {
		this.modules = modules;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}




}

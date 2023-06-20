package eu.fbk.dslab.playful.engine.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="learning-scenario")
public class LearningScenario {
	@Id
	private String id;
	@Indexed
	private String domainId;	
	private String title;
	private String desc;
	private String language;
	private List<String> learners = new ArrayList<>();
	private List<String> educators = new ArrayList<>();

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

	public List<String> getLearners() {
		return learners;
	}

	public void setLearners(List<String> learners) {
		this.learners = learners;
	}

	public List<String> getEducators() {
		return educators;
	}

	public void setEducators(List<String> educators) {
		this.educators = educators;
	}


}

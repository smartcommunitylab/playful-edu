package eu.fbk.dslab.playful.engine.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="learning-fragments")
public class LearningFragment {
	public static enum Type {
		singleton, set, list
	};
	
	@Id
	private String id;
	@Indexed
	private String domainId;	
	@Indexed
	private String learningModuleId;
	private String title;
	private String desc;
	private Type type;

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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getLearningModuleId() {
		return learningModuleId;
	}

	public void setLearningModuleId(String learningModuleId) {
		this.learningModuleId = learningModuleId;
	}



}

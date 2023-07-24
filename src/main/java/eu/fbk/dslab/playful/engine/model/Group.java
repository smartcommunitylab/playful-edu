package eu.fbk.dslab.playful.engine.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="groups")
public class Group {
	@Id
	private String id;
	@Indexed
	private String domainId;
	@Indexed
	private String extId;
	private List<String> learners = new ArrayList<>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDomainId() {
		return domainId;
	}
	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}
	public String getExtId() {
		return extId;
	}
	public void setExtId(String extId) {
		this.extId = extId;
	}
	public List<String> getLearners() {
		return learners;
	}
	public void setLearners(List<String> learners) {
		this.learners = learners;
	}
	

}

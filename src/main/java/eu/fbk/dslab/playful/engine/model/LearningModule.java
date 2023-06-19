package eu.fbk.dslab.playful.engine.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="modules")
public class LearningModule {
	public static enum Level {
		beginner
	};
	
	@Id
	private String id;
	@Indexed
	private String domainId;
	@Indexed
	private String learningScenarioId;
	private String title;
	private String desc;
	private Level level;
	private Date dateFrom;
	private Date dateTo;

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

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public String getLearningScenarioId() {
		return learningScenarioId;
	}

	public void setLearningScenarioId(String learningScenarioId) {
		this.learningScenarioId = learningScenarioId;
	}



}

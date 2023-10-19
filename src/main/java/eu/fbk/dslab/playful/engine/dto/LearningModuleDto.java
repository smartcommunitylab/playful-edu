package eu.fbk.dslab.playful.engine.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.fbk.dslab.playful.engine.model.LearningModule;
import eu.fbk.dslab.playful.engine.model.LearningModule.Level;

public class LearningModuleDto {
	private String id;
	private String title;
	private String desc;
	private Level level;
	private Date dateFrom;
	private Date dateTo;
	private List<LearningFragmentDto> fragments = new ArrayList<>();
	
	public LearningModuleDto() {}
	
	public LearningModuleDto(LearningModule module) {
		this.id = module.getId();
		this.title = module.getTitle();
		this.desc = module.getDesc();
		this.level = module.getLevel();
		this.dateFrom = module.getDateFrom();
		this.dateTo = module.getDateTo();
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<LearningFragmentDto> getFragments() {
		return fragments;
	}

	public void setFragments(List<LearningFragmentDto> fragments) {
		this.fragments = fragments;
	}



}

package eu.fbk.dslab.playful.engine.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.fbk.dslab.playful.engine.model.Educator;
import eu.fbk.dslab.playful.engine.model.ExternalActivity;
import eu.fbk.dslab.playful.engine.model.Group;
import eu.fbk.dslab.playful.engine.model.Learner;
import eu.fbk.dslab.playful.engine.repository.EducatorRepository;
import eu.fbk.dslab.playful.engine.repository.ExternalActivityRepository;
import eu.fbk.dslab.playful.engine.repository.GroupRepository;
import eu.fbk.dslab.playful.engine.repository.LearnerRepository;

@Service
public class IntegrationService {
	private static transient final Logger logger = LoggerFactory.getLogger(IntegrationService.class);
	
    @Autowired
    EducatorRepository educatorRepository;
    @Autowired
    LearnerRepository learnerRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    ExternalActivityRepository externalActivityRepository;
    
    public void importEducators(String domainId, List<Educator> educators) {
    	for(Educator educator : educators) {
    		educator.setDomainId(domainId);
    		Educator ed = educatorRepository.findOneByDomainIdAndNickname(domainId, educator.getNickname());
			if(ed == null) {
				ed = new Educator();
				ed.setDomainId(domainId);
				ed.setNickname(educator.getNickname());
			}
    		ed.setEmail(educator.getEmail());
    		ed.setFirstname(educator.getFirstname());
    		ed.setLastname(educator.getLastname());
    		educatorRepository.save(ed);
    		logger.info(String.format("importEducators[%s]:%s", domainId, ed.getId()));
    	}
    }
    
    public void importLearners(String domainId, List<Learner> learners) {
    	for(Learner learner : learners) {
    		learner.setDomainId(domainId);
    		Learner l = learnerRepository.findOneByDomainIdAndNickname(domainId, learner.getNickname());
    		if(l == null) {
    			l = new Learner();
    			l.setDomainId(domainId);
    			l.setNickname(learner.getNickname());
    		}
    		l.setEmail(learner.getEmail());
    		l.setFirstname(learner.getFirstname());
    		l.setLastname(learner.getLastname());
    		learnerRepository.save(l); 
    		logger.info(String.format("importLearners[%s]:%s", domainId, l.getId()));
    	}
    }
    
    public void importGroups(String domainId, List<Group> groups) {
    	for(Group group : groups) {
    		group.setDomainId(domainId);
    		Group g = groupRepository.findOneByDomainIdAndExtId(domainId, group.getExtId());
    		if(g == null) {
    			g = new Group();
    			g.setDomainId(domainId);
    			g.setExtId(group.getExtId());
    		}
    		g.setName(group.getName());
    		g.getLearners().clear();
    		for(String nickname : group.getLearners()) {
    			Learner l = learnerRepository.findOneByDomainIdAndNickname(domainId, nickname);
    			if(l != null) {
    				g.getLearners().add(l.getId());
    			}    			    			
    		}
    		groupRepository.save(g);    
    		logger.info(String.format("importGroups[%s]:%s", domainId, g.getId()));
    	}
    }

    public void importExtActivities(String domainId, List<ExternalActivity> activities) {
    	for(ExternalActivity activity : activities) {
    		activity.setDomainId(domainId);
    		ExternalActivity act = externalActivityRepository.findOneByDomainIdAndExtId(domainId, activity.getExtId());
    		if(act == null) {
    			act = new ExternalActivity();
    			act.setDomainId(domainId);
    			act.setExtId(activity.getExtId());
    		}
    		act.setTitle(activity.getTitle());
    		act.setDesc(activity.getDesc());
    		act.setLanguage(activity.getLanguage());
    		act.setExtUrl(activity.getExtUrl());
    		act.setType(activity.getType());
    		externalActivityRepository.save(act);   
    		logger.info(String.format("importExtActivities[%s]:%s", domainId, act.getId()));
    	}
    }
    
}

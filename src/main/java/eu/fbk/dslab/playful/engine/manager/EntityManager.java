package eu.fbk.dslab.playful.engine.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import eu.fbk.dslab.playful.engine.model.Educator;
import eu.fbk.dslab.playful.engine.model.LearningFragment;
import eu.fbk.dslab.playful.engine.model.LearningModule;
import eu.fbk.dslab.playful.engine.model.LearningScenario;
import eu.fbk.dslab.playful.engine.repository.EducatorRepository;
import eu.fbk.dslab.playful.engine.repository.LearningFragmentRepository;
import eu.fbk.dslab.playful.engine.repository.LearningModuleRepository;
import eu.fbk.dslab.playful.engine.repository.LearningScenarioRepository;
import eu.fbk.dslab.playful.engine.security.SecurityHelper;

@Service
public class EntityManager {

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	EducatorRepository educatorRepository;
	@Autowired
	LearningScenarioRepository learningScenarioRepository;
	@Autowired
	LearningModuleRepository learningModuleRepository;
	@Autowired
	LearningFragmentRepository learningFragmentRepository;
	
	@Autowired
	SecurityHelper securityHelper;
	
	public boolean checkEducator(String learningScenarioId) {
		LearningScenario ls = learningScenarioRepository.findById(learningScenarioId).orElse(null);
		if(ls != null) {
			Educator educator = educatorRepository.findOneByDomainIdAndEmail(ls.getDomainId(), securityHelper.getEmail());
			if((educator != null) && ls.getEducators().contains(educator.getId())) {
				return true;
			}					
		}
		return false;		
	}
	
	public boolean checkEducatorModule(String learningModuleId) {
		LearningModule lm = learningModuleRepository.findById(learningModuleId).orElse(null);
		if(lm != null) {
			LearningScenario ls = learningScenarioRepository.findById(lm.getLearningScenarioId()).orElse(null);
			if(ls != null) {
				Educator educator = educatorRepository.findOneByDomainIdAndEmail(ls.getDomainId(), securityHelper.getEmail());
				if((educator != null) && ls.getEducators().contains(educator.getId())) {
					return true;
				}									
			}
		}
		return false;		
	}
	
	public boolean checkEducatorFragment(String learningFragmentId) {
		LearningFragment lf = learningFragmentRepository.findById(learningFragmentId).orElse(null);
		if(lf != null) {
			LearningModule lm = learningModuleRepository.findById(lf.getLearningModuleId()).orElse(null);
			if(lm != null) {
				LearningScenario ls = learningScenarioRepository.findById(lm.getLearningScenarioId()).orElse(null);
				if(ls != null) {
					Educator educator = educatorRepository.findOneByDomainIdAndEmail(ls.getDomainId(), securityHelper.getEmail());
					if((educator != null) && ls.getEducators().contains(educator.getId())) {
						return true;
					}									
				}
			}			
		}
		return false;		
	}
	
}

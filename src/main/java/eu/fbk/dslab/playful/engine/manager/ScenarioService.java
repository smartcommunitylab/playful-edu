package eu.fbk.dslab.playful.engine.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import eu.fbk.dslab.playful.engine.dto.ActivityDto;
import eu.fbk.dslab.playful.engine.dto.ComposedActivityDto;
import eu.fbk.dslab.playful.engine.dto.LearningFragmentDto;
import eu.fbk.dslab.playful.engine.dto.LearningModuleDto;
import eu.fbk.dslab.playful.engine.dto.LearningScenarioDto;
import eu.fbk.dslab.playful.engine.model.Activity;
import eu.fbk.dslab.playful.engine.model.ComposedActivity;
import eu.fbk.dslab.playful.engine.model.Concept;
import eu.fbk.dslab.playful.engine.model.Educator;
import eu.fbk.dslab.playful.engine.model.ExternalActivity;
import eu.fbk.dslab.playful.engine.model.Learner;
import eu.fbk.dslab.playful.engine.model.LearningFragment;
import eu.fbk.dslab.playful.engine.model.LearningModule;
import eu.fbk.dslab.playful.engine.model.LearningScenario;
import eu.fbk.dslab.playful.engine.repository.ActivityRepository;
import eu.fbk.dslab.playful.engine.repository.ComposedActivityRepository;
import eu.fbk.dslab.playful.engine.repository.ConceptRepository;
import eu.fbk.dslab.playful.engine.repository.EducatorRepository;
import eu.fbk.dslab.playful.engine.repository.ExternalActivityRepository;
import eu.fbk.dslab.playful.engine.repository.LearnerRepository;
import eu.fbk.dslab.playful.engine.repository.LearningFragmentRepository;
import eu.fbk.dslab.playful.engine.repository.LearningModuleRepository;
import eu.fbk.dslab.playful.engine.repository.LearningScenarioRepository;

@Service
public class ScenarioService {
	private static transient final Logger logger = LoggerFactory.getLogger(ScenarioService.class);
	
	@Autowired
	LearningScenarioRepository learningScenarioRepository;
	
	@Autowired
	LearningModuleRepository learningModuleRepository;
	
	@Autowired
	LearningFragmentRepository learningFragmentRepository;
	
	@Autowired
	ComposedActivityRepository composedActivityRepository;
	
	@Autowired
	ActivityRepository activityRepository;
	
	@Autowired
	LearnerRepository learnerRepository;
	
	@Autowired
	EducatorRepository educatorRepository;
	
	@Autowired
	ExternalActivityRepository externalActivityRepository;
	
	@Autowired
	ConceptRepository conceptRepository;
	
	public Learner getLearner(String nickname, String domainId) throws HttpClientErrorException {
		Learner learner = learnerRepository.findOneByDomainIdAndNickname(domainId, nickname);
		if(learner == null)
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
		return learner;
	}
	
	public Educator getEducator(String nickname, String domainId) throws HttpClientErrorException {
		Educator educator = educatorRepository.findOneByDomainIdAndNickname(domainId, nickname);
		if(educator == null)
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
		return educator;
	}

	public List<LearningScenarioDto> getEducatorScenario(String educatorId, String domainId) throws HttpClientErrorException {
		List<LearningScenarioDto> result = new ArrayList<>();
		List<LearningScenario> scenarios = learningScenarioRepository.findByDomainIdAndEducators(domainId, educatorId);
		scenarios.forEach(s -> result.add(getLearningScenario(s.getId())));
		return result;
	}
	
	public List<LearningScenarioDto> getLearnerScenario(String learnerId, String domainId) throws HttpClientErrorException {
		List<LearningScenarioDto> result = new ArrayList<>();
		List<LearningScenario> scenarios = learningScenarioRepository.findByDomainIdAndLearners(domainId, learnerId);
		scenarios.forEach(s -> result.add(getLearningScenario(s.getId())));
		return result;
	}
	
	public List<LearningScenarioDto> getPublicLearningScenario(String domainId) throws HttpClientErrorException {
		List<LearningScenarioDto> result = new ArrayList<>();
		List<LearningScenario> scenarios = learningScenarioRepository.findByDomainIdAndPublicScenario(domainId, true);
		scenarios.forEach(s -> result.add(getLearningScenario(s.getId())));
		return result;
	}
	
	private LearningScenarioDto getLearningScenario(String learningScenarioId) throws HttpClientErrorException {
		LearningScenario learningScenario = learningScenarioRepository.findById(learningScenarioId).orElse(null);
		if(learningScenario != null) {
			LearningScenarioDto scenarioDto = new LearningScenarioDto(learningScenario);
			
			List<Learner> learners = learnerRepository.findByIdIn(learningScenario.getLearners());
			scenarioDto.getLearners().addAll(learners);
			
			List<Educator> educators = educatorRepository.findByIdIn(learningScenario.getEducators());
			scenarioDto.getEducators().addAll(educators);
			
			List<LearningModule> modules = learningModuleRepository.findByLearningScenarioId(learningScenarioId, 
					Sort.by(Direction.ASC, "position"));
			for(LearningModule module : modules) {
				LearningModuleDto moduleDto = new LearningModuleDto(module);
				scenarioDto.getModules().add(moduleDto);
				
				LearningFragment fragment = learningFragmentRepository.findFirstByLearningModuleId(module.getId());
				LearningFragmentDto fragmentDto = new LearningFragmentDto(fragment);
				moduleDto.setFragment(fragmentDto);
				
				List<ComposedActivity> composedActivities = composedActivityRepository.findByLearningFragmentId(fragment.getId(), 
						Sort.by(Direction.ASC, "position"));
				for(ComposedActivity composedActivity : composedActivities) {
					ComposedActivityDto composedActivityDto = new ComposedActivityDto(composedActivity);
					fragmentDto.getComposedActivities().add(composedActivityDto);
					
					List<Activity> activities = activityRepository.findByComposedActivityId(composedActivity.getId());
					for(Activity activity : activities) {
						ActivityDto activityDto = new ActivityDto(activity);
						composedActivityDto.getActivities().add(activityDto);
						
						if(StringUtils.isNotBlank(activity.getExternalActivityId())) {
							ExternalActivity externalActivity = externalActivityRepository.findById(activity.getExternalActivityId()).orElse(null);
							activityDto.setExternalActivity(externalActivity);							
						}
						
						List<Concept> goals = conceptRepository.findByIdIn(activity.getGoals());
						activityDto.getGoals().addAll(goals);
					}
				}
			}
			return scenarioDto;
		} else {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
		}
	}

}

package eu.fbk.dslab.playful.engine.manager;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import eu.fbk.dslab.playful.engine.dto.ComposedActivityRunDto;
import eu.fbk.dslab.playful.engine.model.Activity;
import eu.fbk.dslab.playful.engine.model.Activity.Type;
import eu.fbk.dslab.playful.engine.model.ActivityStatus;
import eu.fbk.dslab.playful.engine.model.ActivityStatus.Status;
import eu.fbk.dslab.playful.engine.model.ComposedActivity;
import eu.fbk.dslab.playful.engine.model.ComposedActivityRun;
import eu.fbk.dslab.playful.engine.model.ExternalActivity;
import eu.fbk.dslab.playful.engine.model.Group;
import eu.fbk.dslab.playful.engine.model.Learner;
import eu.fbk.dslab.playful.engine.model.LearningFragment;
import eu.fbk.dslab.playful.engine.model.LearningFragmentRun;
import eu.fbk.dslab.playful.engine.model.LearningModule;
import eu.fbk.dslab.playful.engine.model.LearningModuleRun;
import eu.fbk.dslab.playful.engine.model.LearningScenario;
import eu.fbk.dslab.playful.engine.model.LearningScenarioRun;
import eu.fbk.dslab.playful.engine.repository.ActivityRepository;
import eu.fbk.dslab.playful.engine.repository.ActivityStatusRepository;
import eu.fbk.dslab.playful.engine.repository.ComposedActivityRepository;
import eu.fbk.dslab.playful.engine.repository.ConceptRepository;
import eu.fbk.dslab.playful.engine.repository.EducatorRepository;
import eu.fbk.dslab.playful.engine.repository.ExternalActivityRepository;
import eu.fbk.dslab.playful.engine.repository.GroupRepository;
import eu.fbk.dslab.playful.engine.repository.LearnerRepository;
import eu.fbk.dslab.playful.engine.repository.LearningFragmentRepository;
import eu.fbk.dslab.playful.engine.repository.LearningModuleRepository;
import eu.fbk.dslab.playful.engine.repository.LearningScenarioRepository;
import eu.fbk.dslab.playful.engine.repository.LearningScenarioRunRepository;

@Service
public class RunningScenarioService {
	private static transient final Logger logger = LoggerFactory.getLogger(RunningScenarioService.class);
	
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
	ActivityStatusRepository activityStatusRepository;
	
	@Autowired
	LearningScenarioRunRepository learningScenarioRunRepository;
	
	@Autowired
	LearnerRepository learnerRepository;
	
	@Autowired
	EducatorRepository educatorRepository;
	
	@Autowired
	ExternalActivityRepository externalActivityRepository;
	
	@Autowired
	ConceptRepository conceptRepository;
	
	@Autowired
	GroupRepository groupRepository;
	
	public LearningScenarioRun getLearningScenarioRun(String domainId, String learningScenarioId, String learnerId) throws HttpClientErrorException {
		LearningScenarioRun scenarioRun = learningScenarioRunRepository.findByLearningScenarioIdAndLearnerId(learningScenarioId, learnerId);
		if(scenarioRun != null) {
			for(LearningModuleRun module : scenarioRun.getModules()) {
				LearningFragmentRun fragment = module.getFragment();
				if(fragment != null) {
					for(ComposedActivityRun composedActivity : fragment.getComposedActivities()) {
						for(String activityStatusId : composedActivity.getActivityStatusIds()) {
							ActivityStatus activityStatus = activityStatusRepository.findById(activityStatusId).orElse(null);
							if(activityStatus != null) {
								composedActivity.getActivities().add(activityStatus);
							}
						}
					}
				}
			}
			return scenarioRun;
		}			
		throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
	}
	
	public List<ActivityStatus> getActivityStatus(List<String> ids) {
		return activityStatusRepository.findByIdIn(ids);
	}
	
	public void runLearningScenario(String learningScenarioId) throws HttpClientErrorException {
		LearningScenario learningScenario = learningScenarioRepository.findById(learningScenarioId).orElse(null);
		if(learningScenario != null) {
			List<Learner> allLearners = learnerRepository.findByIdIn(learningScenario.getLearners());
			for(Learner learner : allLearners) {
				runLearningScenario(learningScenario, learner);
			}
		}
	}
	
	public void runLearnerLearningScenario(String learningScenarioId, String learnerId) {
		LearningScenario learningScenario = learningScenarioRepository.findById(learningScenarioId).orElse(null);
		Learner learner = learnerRepository.findById(learnerId).orElse(null);
		if((learningScenario != null) && (learner != null)) {
			runLearningScenario(learningScenario, learner);
		}
	}
	
	private void runLearningScenario(LearningScenario learningScenario, Learner learner) throws HttpClientErrorException {
		LearningScenarioRun scenarioRun = new LearningScenarioRun();
		scenarioRun.setDomainId(learningScenario.getDomainId());
		scenarioRun.setLearningScenarioId(learningScenario.getId());
		scenarioRun.setStartingDate(new Date());
		scenarioRun.setLearnerId(learner.getId());
		
		List<LearningModule> modules = learningModuleRepository.findByLearningScenarioId(learningScenario.getId(), 
				Sort.by(Direction.ASC, "position"));
		for(LearningModule module : modules) {
			LearningModuleRun moduleRun = new LearningModuleRun();
			moduleRun.setLearningModuleId(module.getId());
			scenarioRun.getModules().add(moduleRun);
			
			LearningFragment fragment = learningFragmentRepository.findFirstByLearningModuleId(module.getId());
			if(fragment != null) {
				LearningFragmentRun fragmentRun = new LearningFragmentRun();
				fragmentRun.setLearningFragmentId(fragment.getId());
				moduleRun.setFragment(fragmentRun);
				
				List<ComposedActivity> composedActivities = composedActivityRepository.findByLearningFragmentId(fragment.getId(), 
						Sort.by(Direction.ASC, "position"));
				for(ComposedActivity composedActivity : composedActivities) {
					ComposedActivityRun composedActivityRun = new ComposedActivityRun();
					composedActivityRun.setComposedActivityId(composedActivity.getId());
					composedActivityRun.setType(composedActivity.getType());
					fragmentRun.getComposedActivities().add(composedActivityRun);
					
					List<Activity> activities = activityRepository.findByComposedActivityId(composedActivity.getId());
					if(composedActivity.getType().equals(ComposedActivity.Type.list)) {
						Comparator<Activity> compareByPosition = 
								(Activity o1, Activity o2) -> Integer.compare(o1.getPosition(), o2.getPosition());
						Collections.sort(activities, compareByPosition);
					}
					for(Activity activity : activities) {
						if(activity.getType().equals(Type.concrete)) {
							ActivityStatus activityStatus = new ActivityStatus();
							activityStatus.setDomainId(learningScenario.getDomainId());
							activityStatus.setActivityId(activity.getId());
							activityStatus.setExternalActivityId(activity.getExternalActivityId());
							activityStatus.setComposedActivityId(composedActivity.getId());
							activityStatus.setLearningFragmentId(fragment.getId());
							activityStatus.setLearningModuleId(module.getId());
							activityStatus.setLearningScenarioId(learningScenario.getId());
							activityStatus.setLearningScenarioRunId(scenarioRun.getId());
							activityStatus.setLearnerId(learner.getId());
							activityStatus.setLastUpdate(new Date());
							activityStatusRepository.save(activityStatus);
							composedActivityRun.getActivityStatusIds().add(activityStatus.getId());
						} else if(activity.getType().equals(Type.group)) { 
							List<ExternalActivity> extActivityList = externalActivityRepository.findByDomainIdAndGroupCorrelator(
									learningScenario.getDomainId(), activity.getGroupCorrelator());
							for(ExternalActivity extActivity : extActivityList) {
								Group group = groupRepository.findOneByDomainIdAndExtId(learningScenario.getDomainId(), 
										extActivity.getExtGroupId());
								if(group != null) {
									if(group.getLearners().contains(learner.getId())) {
										ActivityStatus activityStatus = new ActivityStatus();
										activityStatus.setDomainId(learningScenario.getDomainId());
										activityStatus.setActivityId(activity.getId());
										activityStatus.setExternalActivityId(extActivity.getId());
										activityStatus.setComposedActivityId(composedActivity.getId());
										activityStatus.setLearningFragmentId(fragment.getId());
										activityStatus.setLearningModuleId(module.getId());
										activityStatus.setLearningScenarioId(learningScenario.getId());
										activityStatus.setLearningScenarioRunId(scenarioRun.getId());
										activityStatus.setLearnerId(learner.getId());
										activityStatus.setLastUpdate(new Date());
										activityStatusRepository.save(activityStatus);
										composedActivityRun.getActivityStatusIds().add(activityStatus.getId());
									}									
								}
							}
						} else {
							logger.warn(String.format("skip abstract activity[%s]:%s", activity.getId(), activity.getTitle()));
						}
					}
				}
			}
		}			
		learningScenarioRunRepository.save(scenarioRun);
	}
	
	public ComposedActivityRunDto getNextActivity(String domainId, String learningScenarioId, String learnerId) throws HttpClientErrorException {
		LearningScenarioRun scenarioRun = learningScenarioRunRepository.findByLearningScenarioIdAndLearnerId(learningScenarioId, learnerId);
		if(scenarioRun != null) {
			for(LearningModuleRun moduleRun : scenarioRun.getModules()) {
				LearningFragmentRun fragmentRun = moduleRun.getFragment();
				if(fragmentRun != null) {
					for(ComposedActivityRun activityRun : fragmentRun.getComposedActivities()) {
						ComposedActivityRunDto activityRunDto = new ComposedActivityRunDto(activityRun);					
						List<ActivityStatus> list = activityStatusRepository.findByIdIn(activityRun.getActivityStatusIds());
						boolean found = false;
						for(ActivityStatus activityStatus : list) {
							if(activityStatus.getStatus().equals(Status.assigned)) {
								found = true;
								activityRunDto.getActivities().add(activityStatus);
							}
						}
						if(found) {
							return activityRunDto;
						}
					}					
				}
			}
		}
		throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
	}
	
	public void changeActivityStatus(String domainId, String learningScenarioId, String learnerId, 
			String activityId, Status status) throws HttpClientErrorException {
		LearningScenarioRun scenarioRun = learningScenarioRunRepository.findByLearningScenarioIdAndLearnerId(learningScenarioId, learnerId);
		if(scenarioRun != null) {
			for(LearningModuleRun module : scenarioRun.getModules()) {
				LearningFragmentRun fragment = module.getFragment();
				if(fragment != null) {
					for(ComposedActivityRun composedActivity : fragment.getComposedActivities()) {
						for(String activityStatusId : composedActivity.getActivityStatusIds()) {
							ActivityStatus activityStatus = activityStatusRepository.findById(activityStatusId).orElse(null);
							if(activityStatus != null) {
								if(activityStatus.getActivityId().equals(activityId) && 
										(Status.in_progress.equals(activityStatus.getStatus()) 
												|| Status.assigned.equals(activityStatus.getStatus()))) {
									activityStatus.setStatus(status);
									activityStatusRepository.save(activityStatus);
									return;
								}
							}
						}
					}
				}
			}
		}			
		throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
	}
		
}

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

import eu.fbk.dslab.playful.engine.model.Activity;
import eu.fbk.dslab.playful.engine.model.Activity.Type;
import eu.fbk.dslab.playful.engine.model.ActivityStatus;
import eu.fbk.dslab.playful.engine.model.ActivityStatus.Status;
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
				for(LearningFragmentRun fragment : module.getFragments()) {
					for(String activityStatusId : fragment.getActivityStatusIds()) {
						ActivityStatus activityStatus = activityStatusRepository.findById(activityStatusId).orElse(null);
						if(activityStatus != null) {
							fragment.getActivities().add(activityStatus);
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
			learningScenario.setRunning(true);
			learningScenarioRepository.save(learningScenario);
			List<Learner> allLearners = learnerRepository.findByIdIn(learningScenario.getLearners());
			for(Learner learner : allLearners) {
				runLearningScenario(learningScenario, learner);
			}
		}
	}
	
	public void runLearnerLearningScenario(String learningScenarioId, String learnerId) throws HttpClientErrorException {
		LearningScenario learningScenario = learningScenarioRepository.findById(learningScenarioId).orElse(null);
		Learner learner = learnerRepository.findById(learnerId).orElse(null);
		if((learningScenario != null) && (learner != null) && learningScenario.isRunning()) {
			runLearningScenario(learningScenario, learner);
		}
	}
	
	private void runLearningScenario(LearningScenario learningScenario, Learner learner) throws HttpClientErrorException {
		logger.info(String.format("runLearningScenario[%s]:%s - %s", learningScenario.getDomainId(), learningScenario.getId(), learner.getEmail()));
		//check existing scenario
		LearningScenarioRun oldScenario = learningScenarioRunRepository.findByLearningScenarioIdAndLearnerId(learningScenario.getId(), learner.getId());
		if(oldScenario != null) {
			logger.info(String.format("runLearningScenario[%s] already exists:%s - %s", learningScenario.getDomainId(), 
					learningScenario.getId(), learner.getEmail()));
			return;
		}
		
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
			
			List<LearningFragment> fragments = learningFragmentRepository.findByLearningModuleId(module.getId(), 
					Sort.by(Direction.ASC, "position"));
			for(int i=0; i < fragments.size(); i++) {
				LearningFragment fragment = fragments.get(i);
				LearningFragmentRun fragmentRun = new LearningFragmentRun();
				fragmentRun.setLearningFragmentId(fragment.getId());
				fragmentRun.setType(fragment.getType());
				fragmentRun.setSetCompletionRule(fragment.getSetCompletionRule());
				if(i == 0) {
					fragmentRun.setOpen(true);
				}
				moduleRun.getFragments().add(fragmentRun);
				
				List<Activity> activities = activityRepository.findByLearningFragmentId(fragment.getId());
				
				if(LearningFragment.Type.set.equals(fragment.getType()) && 
						LearningFragment.SetCompletionRule.at_least.equals(fragment.getSetCompletionRule())) {
					fragmentRun.setMinActivities(fragment.getMinActivities());
				} else {
					fragmentRun.setMinActivities(activities.size());
				}
				
				if(fragment.getType().equals(LearningFragment.Type.list)) {
					Comparator<Activity> compareByPosition = 
							(Activity o1, Activity o2) -> Integer.compare(o1.getPosition(), o2.getPosition());
					Collections.sort(activities, compareByPosition);
				}
				for(int j=0; j < activities.size(); j++) {
					Activity activity = activities.get(j);
					if(activity.getType().equals(Type.concrete)) {
						ActivityStatus activityStatus = new ActivityStatus();
						activityStatus.setDomainId(learningScenario.getDomainId());
						activityStatus.setActivityId(activity.getId());
						activityStatus.setExternalActivityId(activity.getExternalActivityId());
						activityStatus.setLearningFragmentId(fragment.getId());
						activityStatus.setLearningModuleId(module.getId());
						activityStatus.setLearningScenarioId(learningScenario.getId());
						activityStatus.setLearningScenarioRunId(scenarioRun.getId());
						activityStatus.setLearnerId(learner.getId());
						activityStatus.setLastUpdate(new Date());
						activityStatus.setOpen(setInitActivityOpen(i, j, fragment.getType()));
						activityStatusRepository.save(activityStatus);
						fragmentRun.getActivityStatusIds().add(activityStatus.getId());
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
									activityStatus.setLearningFragmentId(fragment.getId());
									activityStatus.setLearningModuleId(module.getId());
									activityStatus.setLearningScenarioId(learningScenario.getId());
									activityStatus.setLearningScenarioRunId(scenarioRun.getId());
									activityStatus.setLearnerId(learner.getId());
									activityStatus.setLastUpdate(new Date());
									activityStatus.setOpen(setInitActivityOpen(i, j, fragment.getType()));
									activityStatusRepository.save(activityStatus);
									fragmentRun.getActivityStatusIds().add(activityStatus.getId());
								}									
							}
						}
					} else {
						logger.warn(String.format("skip abstract activity[%s]:%s", activity.getId(), activity.getTitle()));
					}
				}
			}
		}			
		learningScenarioRunRepository.save(scenarioRun);
	}
	
	private boolean setInitActivityOpen(int fragmentIndex, int activityIndex, LearningFragment.Type type) {
		if(fragmentIndex == 0) {
			if(LearningFragment.Type.list.equals(type)) {
				if(activityIndex == 0) {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}
	
	public LearningFragmentRun getNextActivity(String domainId, String learningScenarioId, String learnerId) throws HttpClientErrorException {
		LearningScenarioRun scenarioRun = learningScenarioRunRepository.findByLearningScenarioIdAndLearnerId(learningScenarioId, learnerId);
		if(scenarioRun != null) {
			for(LearningModuleRun moduleRun : scenarioRun.getModules()) {
				for(LearningFragmentRun fragmentRun : moduleRun.getFragments()) {
					List<ActivityStatus> list = activityStatusRepository.findByIdIn(fragmentRun.getActivityStatusIds());
					boolean found = false;
					for(ActivityStatus activityStatus : list) {
						if(activityStatus.getStatus().equals(Status.assigned) && activityStatus.isOpen()) {
							found = true;
							fragmentRun.getActivities().add(activityStatus);
						}
					}
					if(found) {
						return fragmentRun;
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
				for(LearningFragmentRun fragment : module.getFragments()) {
					for(String activityStatusId : fragment.getActivityStatusIds()) {
						ActivityStatus activityStatus = activityStatusRepository.findById(activityStatusId).orElse(null);
						if(activityStatus != null) {
							if(activityStatus.getActivityId().equals(activityId) && 
									(Status.in_progress.equals(activityStatus.getStatus()) 
											|| Status.assigned.equals(activityStatus.getStatus()))) {
								activityStatus.setStatus(status);
								activityStatus.setLastUpdate(new Date());
								activityStatusRepository.save(activityStatus);
								if(Status.completed.equals(status)) {
									//check if fragment is completed
									completeActivityinFragment(scenarioRun, fragment);
								}
								return;
							}
						}
					}					
				}
			}
		}			
		throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
	}
	
	private void completeActivityinFragment(LearningScenarioRun scenarioRun, LearningFragmentRun fragment) {
		//number of completed activities
		int completedActivities = 0;
		for(String activityStatusId : fragment.getActivityStatusIds()) {
			ActivityStatus activityStatus = activityStatusRepository.findById(activityStatusId).orElse(null);
			if(activityStatus != null) {
				if(Status.completed.equals(activityStatus.getStatus())) {
					completedActivities++;
				}
			}
		}
		for(int moduleIndex=0;  moduleIndex < scenarioRun.getModules().size(); moduleIndex++) {
			LearningModuleRun moduleRun = scenarioRun.getModules().get(moduleIndex);
			for(int fragmentIndex=0; fragmentIndex < moduleRun.getFragments().size(); fragmentIndex++) {
				LearningFragmentRun fragmentRun = moduleRun.getFragments().get(fragmentIndex);
				if(fragmentRun.getLearningFragmentId().equals(fragment.getLearningFragmentId())) {
					if(completedActivities >= fragment.getMinActivities()) {
						//set fragment as completed
						fragmentRun.setCompleted(true);
						//set next fragment to open
						if(fragmentIndex < (moduleRun.getFragments().size() - 1)) {
							LearningFragmentRun nextFragmentRun = moduleRun.getFragments().get(fragmentIndex + 1);
							nextFragmentRun.setOpen(true);
							//set fragment activities open
							setFragmentActivitiesOpen(nextFragmentRun);
						} else {
							//set next module open
							if(moduleIndex < (scenarioRun.getModules().size() - 1)) {
								LearningModuleRun nextModuleRun = scenarioRun.getModules().get(moduleIndex + 1);
								if(nextModuleRun.getFragments().size() > 0) {
									LearningFragmentRun nextFragmentRun = nextModuleRun.getFragments().get(0);
									nextFragmentRun.setOpen(true);
									//set fragment activities open
									setFragmentActivitiesOpen(nextFragmentRun);
								}
							}
						}						
						learningScenarioRunRepository.save(scenarioRun);
					} else {
						if(LearningFragment.Type.list.equals(fragmentRun.getType())) {
							//set next activity open
							setListNextActivityOpen(fragmentRun);
						}
					}
				}
			}
		}		
	}
	
	private void setFragmentActivitiesOpen(LearningFragmentRun fragment)  {
		for(int i=0; i < fragment.getActivityStatusIds().size(); i++) {
			String activityStatusId = fragment.getActivityStatusIds().get(i);
			ActivityStatus activityStatus = activityStatusRepository.findById(activityStatusId).orElse(null);
			if(activityStatus != null) {
				if((LearningFragment.Type.list.equals(fragment.getType()) && (i == 0)) ||
						LearningFragment.Type.set.equals(fragment.getType()) ||
						LearningFragment.Type.singleton.equals(fragment.getType())) {
					if(!activityStatus.isOpen()) {
						activityStatus.setOpen(true);
						activityStatusRepository.save(activityStatus);
					}										
				}				
			}
		}
	}
	
	private void setListNextActivityOpen(LearningFragmentRun fragment) {
		for(String activityStatusId : fragment.getActivityStatusIds()) {
			ActivityStatus activityStatus = activityStatusRepository.findById(activityStatusId).orElse(null);
			if(activityStatus != null) {
				if(!activityStatus.isOpen()) {
					activityStatus.setOpen(true);
					activityStatusRepository.save(activityStatus);
					return;
				}
			}
		}
	}
	
		
}

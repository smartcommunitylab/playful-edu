package eu.fbk.dslab.playful.engine.integration;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.fbk.dslab.playful.engine.model.Educator;
import eu.fbk.dslab.playful.engine.model.ExternalActivity;
import eu.fbk.dslab.playful.engine.model.ExternalActivity.Type;
import eu.fbk.dslab.playful.engine.model.Group;
import eu.fbk.dslab.playful.engine.model.Learner;
import eu.fbk.dslab.playful.engine.repository.EducatorRepository;
import eu.fbk.dslab.playful.engine.repository.ExtModuleConfRepository;
import eu.fbk.dslab.playful.engine.repository.ExternalActivityRepository;
import eu.fbk.dslab.playful.engine.repository.GroupRepository;
import eu.fbk.dslab.playful.engine.repository.LearnerRepository;

@Service
public class StandByMeService implements ApplicationListener<ContextRefreshedEvent> {
	private static transient final Logger logger = LoggerFactory.getLogger(StandByMeService.class);
	
	static final String extModuleConf = "STADBYME";
	
    @Autowired
    RestTemplate restTemplate;
    
    @Autowired
    EducatorRepository educatorRepository;
    @Autowired
    LearnerRepository learnerRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    ExternalActivityRepository externalActivityRepository;
    
    @Autowired
    ExtModuleConfRepository extModuleConfRepository;
    
    @Autowired
    TaskScheduler taskScheduler;
    
    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
    String endpoint = null;
    
    public void getEducators(String domainId) {
    	String address = endpoint + "/educators";
    	try {
    		ResponseEntity<String> res = restTemplate.exchange(address, HttpMethod.GET, 
    				new HttpEntity<Object>(null, createHeaders()), String.class);
    		if (!res.getStatusCode().is2xxSuccessful()) {
    			logger.error(String.format("getEducators:[%s] %s", res.getStatusCode(), res.getBody()));
    		}
    		JsonNode jsonNode = mapper.readTree(res.getBody());
    		Iterator<JsonNode> elements = jsonNode.elements();
    		while(elements.hasNext()) {
    			JsonNode node = elements.next();
    			Educator ed = educatorRepository.findOneByDomainIdAndNickname(domainId, getField(node, "login"));
    			if(ed == null) {
    				ed = new Educator();
    				ed.setDomainId(domainId);
    				ed.setNickname(getField(node, "login"));
    			}
    			ed.setEmail(getField(node, "email"));
    			ed.setFirstname(getField(node, "displayname"));
    			educatorRepository.save(ed);
    		}
		} catch (Exception e) {
			logger.error(String.format("getEducators:%s", e.getMessage()));
		}
    }
    
    public void getLearners(String domainId) {
    	String address = endpoint + "/learners";
    	try {
    		ResponseEntity<String> res = restTemplate.exchange(address, HttpMethod.GET, 
    				new HttpEntity<Object>(null, createHeaders()), String.class);
    		if (!res.getStatusCode().is2xxSuccessful()) {
    			logger.error(String.format("getLearners:[%s] %s", res.getStatusCode(), res.getBody()));
    		}
    		JsonNode jsonNode = mapper.readTree(res.getBody());
    		Iterator<JsonNode> elements = jsonNode.elements();
    		while(elements.hasNext()) {
    			JsonNode node = elements.next();
    			Learner l = learnerRepository.findOneByDomainIdAndNickname(domainId, getField(node, "login"));
    			if(l == null) {
    				l = new Learner();
    				l.setDomainId(domainId);
    				l.setNickname(getField(node, "login"));
    			}
    			l.setEmail(getField(node, "email"));
    			l.setFirstname(getField(node, "displayname"));
    			learnerRepository.save(l);
    		}
		} catch (Exception e) {
			logger.error(String.format("getLearners:%s", e.getMessage()));
		}
    }
    
    public void getGroups(String domainId) {
    	String address = endpoint + "/groups";
    	try {
    		ResponseEntity<String> res = restTemplate.exchange(address, HttpMethod.GET, 
    				new HttpEntity<Object>(null, createHeaders()), String.class);
    		if (!res.getStatusCode().is2xxSuccessful()) {
    			logger.error(String.format("getGroups:[%s] %s", res.getStatusCode(), res.getBody()));
    		}
    		JsonNode jsonNode = mapper.readTree(res.getBody());
    		Iterator<JsonNode> elements = jsonNode.elements();
    		while(elements.hasNext()) {
    			JsonNode node = elements.next();
    			Group g = groupRepository.findOneByDomainIdAndExtId(domainId, getField(node, "id"));
    			if(g == null) {
    				g = new Group();
    				g.setDomainId(domainId);
    				g.setExtId(getField(node, "id"));
    			}
    			g.setName(getField(node, "name"));
    			g.getLearners().clear();
    			JsonNode membersNode = mapper.readTree(node.get("members").asText());
    			Iterator<JsonNode> members = membersNode.elements();
    			while(members.hasNext()) {
    				JsonNode memberNode = members.next();
    				String learnerLogin = getField(memberNode, "login");
    				Learner l = learnerRepository.findOneByDomainIdAndNickname(domainId, learnerLogin);
    				if(l != null) {
    					g.getLearners().add(l.getId());
    				}
    			}
    			groupRepository.save(g);
    		}
		} catch (Exception e) {
			logger.error(String.format("getGroups:%s", e.getMessage()));
		}    	
    }

    public void getActivities(String domainId) {
    	String address = endpoint + "/activities";
    	try {
    		ResponseEntity<String> res = restTemplate.exchange(address, HttpMethod.GET, 
    				new HttpEntity<Object>(null, createHeaders()), String.class);
    		if (!res.getStatusCode().is2xxSuccessful()) {
    			logger.error(String.format("getActivities:[%s] %s", res.getStatusCode(), res.getBody()));
    		}
    		JsonNode jsonNode = mapper.readTree(res.getBody());
    		Iterator<JsonNode> elements = jsonNode.elements();
    		while(elements.hasNext()) {
    			JsonNode node = elements.next();
    			ExternalActivity act = externalActivityRepository.findOneByDomainIdAndExtId(domainId, getField(node, "id"));
    			if(act == null) {
    				act = new ExternalActivity();
    				act.setDomainId(domainId);
    				act.setExtId(getField(node, "id"));
    			}
    			act.setTitle(getField(node, "title"));
    			act.setDesc(getField(node, "description"));
    			act.setLanguage(getField(node, "language"));
    			act.setExtUrl(getField(node, "url"));
    			act.setType(Type.individual);
    			String groupAct = getField(node, "group");
    			if(StringUtils.isNotBlank(groupAct)) {
    				if(groupAct.equals("yes")) {
    					act.setType(Type.group);
    				}
    			}
    			externalActivityRepository.save(act);
    		}
		} catch (Exception e) {
			logger.error(String.format("getActivities:%s", e.getMessage()));
		}
    }
    
    public void importData(List<String> domains) {
    	domains.forEach(domainId -> {
    		getEducators(domainId);
    		getLearners(domainId);
    		getGroups(domainId);
    		getActivities(domainId);
    	});
    }

    String getField(JsonNode node, String filed) {
    	return node.get(filed).asText().replace("\"", "");
    }
    
	HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		return headers;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ExtModuleConf conf = extModuleConfRepository.findById(extModuleConf).orElse(null);
		if(conf != null) {
			this.endpoint = conf.getEndpoint();
			ImportTask task = new ImportTask(conf.getDomains());
			taskScheduler.schedule(task, new CronTrigger(conf.getCron()));
		}
	}	
    
	private class ImportTask implements Runnable {
		List<String> domains;
		
		public ImportTask(List<String> domains) {
			this.domains = domains;
		}
		
		@Override
		public void run() {
			importData(this.domains);
		}
		
	}
}

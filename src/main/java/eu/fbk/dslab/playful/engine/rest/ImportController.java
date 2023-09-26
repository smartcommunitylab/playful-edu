package eu.fbk.dslab.playful.engine.rest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import eu.fbk.dslab.playful.engine.model.Educator;
import eu.fbk.dslab.playful.engine.model.ExternalActivity;
import eu.fbk.dslab.playful.engine.model.ExternalActivity.Difficulty;
import eu.fbk.dslab.playful.engine.model.ExternalActivity.Tool;
import eu.fbk.dslab.playful.engine.model.ExternalActivity.Type;
import eu.fbk.dslab.playful.engine.model.Group;
import eu.fbk.dslab.playful.engine.model.Learner;
import eu.fbk.dslab.playful.engine.repository.EducatorRepository;
import eu.fbk.dslab.playful.engine.repository.ExternalActivityRepository;
import eu.fbk.dslab.playful.engine.repository.GroupRepository;
import eu.fbk.dslab.playful.engine.repository.LearnerRepository;
import eu.fbk.dslab.playful.engine.security.UserRole.Role;

@RestController
public class ImportController extends PlayfulController {

	@Autowired
	LearnerRepository learnerRepository;
	
	@Autowired
	EducatorRepository educatorRepository;
	
	@Autowired
	GroupRepository groupRepository;
	
	@Autowired
	ExternalActivityRepository externalActivityRepository;

	@PostMapping(value = "/api/import/learner/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<List<String>> uploadLearner(
			@RequestParam String domainId,
			@RequestParam("data") MultipartFile data) throws Exception {
		securityHelper.checkRole(domainId, Role.domain);
		List<String> result = new ArrayList<>();
		MappingIterator<Map<String, String>> iterator = null;
		try {
			iterator = readCsv(data);
		} catch (Exception e) {
			result.add(e.getMessage());
		}
		int lineCount = 1;
		List<Learner> entities = new ArrayList<>();
		try {
			while(iterator.hasNextValue()) {
				Map<String, String> map = iterator.next();
				String firstname = map.get("firstname").trim();
				String lastname = map.get("lastname").trim();
				String email = map.get("email").trim();
				String nickname = map.get("nickname").trim();
				Learner entity = learnerRepository.findOneByDomainIdAndNickname(domainId, nickname);
				if(entity != null) {
					result.add(String.format("error on line [%s]: %s", lineCount, "nickname already exists"));	
				} else {
					entity = new Learner();
					entity.setDomainId(domainId);
					entity.setFirstname(firstname);
					entity.setLastname(lastname);
					entity.setEmail(email);
					entity.setNickname(nickname);
					entities.add(entity);
				}
				lineCount++;
			}
		} catch (Exception e) {
			result.add(String.format("error on line [%s]: %s", lineCount, e.getMessage()));
		}
		if(result.size() == 0) {
			learnerRepository.saveAll(entities);
		}
		return ResponseEntity.ok(result);
	}

	@PostMapping(value = "/api/import/educator/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<List<String>> uploadEducator(
			@RequestParam String domainId,
			@RequestParam("data") MultipartFile data) throws Exception {
		securityHelper.checkRole(domainId, Role.domain);
		List<String> result = new ArrayList<>();
		MappingIterator<Map<String, String>> iterator = null;
		try {
			iterator = readCsv(data);
		} catch (Exception e) {
			result.add(e.getMessage());
		}
		int lineCount = 1;
		List<Educator> entities = new ArrayList<>();
		try {
			while(iterator.hasNextValue()) {
				Map<String, String> map = iterator.next();
				String firstname = map.get("firstname").trim();
				String lastname = map.get("lastname").trim();
				String email = map.get("email").trim();
				String nickname = map.get("nickname").trim();
				Educator entity = educatorRepository.findOneByDomainIdAndNickname(domainId, nickname);
				if(entity != null) {
					result.add(String.format("error on line [%s]: %s", lineCount, "nickname already exists"));	
				} else {
					entity = new Educator();
					entity.setDomainId(domainId);
					entity.setFirstname(firstname);
					entity.setLastname(lastname);
					entity.setEmail(email);
					entity.setNickname(nickname);
					entities.add(entity);
				}
				lineCount++;
			}
		} catch (Exception e) {
			result.add(String.format("error on line [%s]: %s", lineCount, e.getMessage()));
		}
		if(result.size() == 0) {
			educatorRepository.saveAll(entities);
		}
		return ResponseEntity.ok(result);
	}
	
	@PostMapping(value = "/api/import/group/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<List<String>> uploadGroup(
			@RequestParam String domainId,
			@RequestParam("data") MultipartFile data) throws Exception {
		securityHelper.checkRole(domainId, Role.domain);
		List<String> result = new ArrayList<>();
		MappingIterator<Map<String, String>> iterator = null;
		try {
			iterator = readCsv(data);
		} catch (Exception e) {
			result.add(e.getMessage());
		}
		int lineCount = 1;
		try {
			while(iterator.hasNextValue()) {
				Map<String, String> map = iterator.next();
				String extId = map.get("extId").trim();
				String nickname = map.get("nickname").trim();
				Group group = groupRepository.findOneByDomainIdAndExtId(domainId, extId);
				if(group == null) {
					group = new Group();
					group.setDomainId(domainId);
					group.setExtId(extId);
					groupRepository.save(group);
				}
				Learner learner = learnerRepository.findOneByDomainIdAndNickname(domainId, nickname);
				if(learner == null) {
					result.add(String.format("error on line [%s]: %s", lineCount, "nickname not found"));
				} else {
					if(!group.getLearners().contains(learner.getId())) {
						group.getLearners().add(learner.getId());
						groupRepository.save(group);
					}
				}
				lineCount++;
			}
		} catch (Exception e) {
			result.add(String.format("error on line [%s]: %s", lineCount, e.getMessage()));
		}
		return ResponseEntity.ok(result);
	}
	
	@PostMapping(value = "/api/import/extactivity/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<List<String>> uploadExternalActivity(
			@RequestParam String domainId,
			@RequestParam("data") MultipartFile data) throws Exception {
		securityHelper.checkRole(domainId, Role.domain);
		List<String> result = new ArrayList<>();
		MappingIterator<Map<String, String>> iterator = null;
		try {
			iterator = readCsv(data);
		} catch (Exception e) {
			result.add(e.getMessage());
		}
		int lineCount = 1;
		List<ExternalActivity> entities = new ArrayList<>();
		try {
			while(iterator.hasNextValue()) {
				Map<String, String> map = iterator.next();
				String title = map.get("title").trim();
				String desc = map.get("desc").trim();
				String language = map.get("language").trim();
				String type = map.get("type").trim();
				String tool = map.get("tool").trim();
				String difficulty = map.get("difficulty").trim();
				String groupCorrelator = map.get("groupCorrelator").trim();
				String extId = map.get("extId").trim();
				String extGroupId = map.get("extGroupId").trim();
				String extUrl = map.get("extUrl").trim();
				ExternalActivity entity = externalActivityRepository.findOneByDomainIdAndExtId(domainId, extId);
				if(entity != null) {
					result.add(String.format("error on line [%s]: %s", lineCount, "extId already exists"));
				} else {
					entity = new ExternalActivity();
					entity.setDomainId(domainId);
					entity.setTitle(title);
					entity.setDesc(desc);
					entity.setLanguage(language);
					entity.setType(Type.valueOf(type));
					entity.setTool(Tool.valueOf(tool));
					entity.setDifficulty(Difficulty.valueOf(difficulty));
					entity.setGroupCorrelator(groupCorrelator);
					entity.setExtId(extId);
					entity.setExtGroupId(extGroupId);
					entity.setExtUrl(extUrl);
					entities.add(entity);					
				}
				lineCount++;
			}
		} catch (Exception e) {
			result.add(String.format("error on line [%s]: %s", lineCount, e.getMessage()));
		}
		if(result.size() == 0) {
			externalActivityRepository.saveAll(entities);
		}
		return ResponseEntity.ok(result);
	}
	
	private MappingIterator<Map<String, String>> readCsv(MultipartFile data)
			throws UnsupportedEncodingException, IOException {
		InputStreamReader contentReader = new InputStreamReader(data.getInputStream(), "UTF-8");
		CsvMapper csvMapper = new CsvMapper();
		CsvSchema csvSchema = CsvSchema.emptySchema().withHeader();
		MappingIterator<Map<String, String>> iterator = csvMapper.readerFor(Map.class)
				.with(csvSchema).readValues(contentReader);
		return iterator;
	}

}

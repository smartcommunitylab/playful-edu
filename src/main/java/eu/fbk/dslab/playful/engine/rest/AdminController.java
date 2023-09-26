package eu.fbk.dslab.playful.engine.rest;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.fbk.dslab.playful.engine.repository.UserRoleRepository;
import eu.fbk.dslab.playful.engine.security.UserRole;
import eu.fbk.dslab.playful.engine.utils.Utils;

@RestController
public class AdminController extends PlayfulController {
	@Autowired
	UserRoleRepository userRoleRepository;
	
	@GetMapping("/api/roles")
	public Page<UserRole> getList(
			@RequestParam(required = false) String text,
			@ParameterObject Pageable pageRequest) throws Exception {
		securityHelper.checkAdminRole();
		if(Utils.isNotEmpty(text)) {
			return userRoleRepository.findByText(text, pageRequest);
		} else {
			return userRoleRepository.findAll(pageRequest);
		}
		
	}
	
	@PostMapping("/api/roles")
	public UserRole create(@RequestBody UserRole role) throws Exception {
		securityHelper.checkAdminRole();
		return userRoleRepository.save(role);
	}
	
	@PutMapping("/api/role/{id}")
	public UserRole update(@PathVariable String id,
			@RequestBody UserRole role) throws Exception {
		securityHelper.checkAdminRole();
		role.setId(id);
		return userRoleRepository.save(role);
	}
	
	@DeleteMapping("/api/role/{id}")
	public UserRole delete(@PathVariable String id) throws Exception {
		securityHelper.checkAdminRole();
		UserRole role = userRoleRepository.findById(id).orElse(null);
		if(role != null) {
			userRoleRepository.deleteById(id);
		}
		return role;
	}

}

package eu.fbk.dslab.playful.engine.security;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import eu.fbk.dslab.playful.engine.exception.UnauthorizedException;
import eu.fbk.dslab.playful.engine.repository.UserRoleRepository;
import eu.fbk.dslab.playful.engine.security.UserRole.Role;
import eu.fbk.dslab.playful.engine.utils.Utils;

@Service
public class SecurityHelper {
	static Log logger = LogFactory.getLog(SecurityHelper.class);
	
	@Autowired
	UserRoleRepository userRoleRepository;
	
	LoadingCache<String, List<UserRole>> roleCache = CacheBuilder.newBuilder()
			.expireAfterWrite(30, TimeUnit.MINUTES)
			.build(new CacheLoader<String, List<UserRole>>() {
				@Override
				public List<UserRole> load(String key) throws Exception {
					List<UserRole> list = userRoleRepository.findByPreferredUsername(key);
					return list; 
			}
	});

	public String getCurrentPreferredUsername() throws UnauthorizedException {
		JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		Jwt principal = (Jwt) authentication.getPrincipal();
		String subject = principal.getClaimAsString("preferred_username");
		if(Utils.isEmpty(subject)) {
			throw new UnauthorizedException("preferred_username not found");
		}
		return subject;
	}
	
	private boolean checkAdmin(String username) {
		List<UserRole> list;
		try {
			list = roleCache.get(username);
			return list.contains(Role.admin);
		} catch (ExecutionException e) {
			logger.warn(String.format("checkRole [%s]:%s", username, e.getMessage()));
		}
		return false;		
	}
	
	private boolean checkRole(String username, Role role, String entityId) {
		List<UserRole> list;
		try {
			list = roleCache.get(username);
			for(UserRole r : list) {
				if(r.getRole().equals(role) && r.getEntityId().equals(entityId)) {
					return true;
				}
			}
		} catch (ExecutionException e) {
			logger.warn(String.format("checkRole [%s]:%s", username, e.getMessage()));
		}
		return false;
	}

}

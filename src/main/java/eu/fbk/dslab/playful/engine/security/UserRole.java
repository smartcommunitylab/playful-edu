package eu.fbk.dslab.playful.engine.security;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="roles")
public class UserRole {
	public static enum Role {
		admin, domain, educator
	};

	@Id
	private String id;
	
	@Indexed
	private String preferredUsername;
	private Role role;
	private String entityId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPreferredUsername() {
		return preferredUsername;
	}
	public void setPreferredUsername(String preferredUsername) {
		this.preferredUsername = preferredUsername;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	
}

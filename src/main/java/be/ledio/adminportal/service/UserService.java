package be.ledio.adminportal.service;

import java.util.Set;

import be.ledio.adminportal.model.User;
import be.ledio.adminportal.model.security.PasswordResetToken;
import be.ledio.adminportal.model.security.UserRole;

// Just here to allorw Dependency Injection...
public interface UserService {
	PasswordResetToken getPasswordResetToken(final String token);

	void createPasswordResetTokenForUser(final User user, final String token);
	
	User findByUsername(String username);
	
	User findByEmail(String Email);
	
	User createUser(User user, Set<UserRole> userRoles) throws Exception;
	
	User save(User user);
}

package be.ledio.adminportal.service.impl;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ledio.adminportal.model.User;
import be.ledio.adminportal.model.security.PasswordResetToken;
import be.ledio.adminportal.model.security.UserRole;
import be.ledio.adminportal.repository.PasswordResetTokenRepository;
import be.ledio.adminportal.repository.RoleRepository;
import be.ledio.adminportal.repository.UserRepository;
import be.ledio.adminportal.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	// Find a PasswordResetToken entity in DB by the token field...
	public PasswordResetToken getPasswordResetToken(final String token) {
		return passwordResetTokenRepository.findByToken(token);
	}

	@Override
	public void createPasswordResetTokenForUser(final User user, final String token) {
		final PasswordResetToken myToken = new PasswordResetToken(token, user);
		// This method is avaiable in the repository because it implements JpaRepo which
		// impl PagingAndSortingRepo which impl CrudRepo and which automate CRUD
		// operations...
		passwordResetTokenRepository.save(myToken);
	}

	@Override
	public User findByUsername(String username) {
		User user = userRepository.findByUserName(username);
		return user;
	}

	@Override
	public User findByEmail(String email) {
		User user = userRepository.findByEmail(email);
		return user;
	}

	@Override
	public User createUser(User user, Set<UserRole> userRoles) throws Exception {
		User localUser = userRepository.findByUserName(user.getUserName());

		// My fault == if (localUser.getUserName() != null) {
		// If above the localUser is null because the repo found nothing we can't execute the "getUserName()"
		if (localUser != null) {
			LOG.info("user {} already exists. Nothing will be done.", user.getUsername());
		} else {			
			// Add role to the "role" table from "UserRole" object
			for (UserRole ur : userRoles) {
				roleRepository.save(ur.getRole());
			}
			// Fill the userRoles field of user with new roles created from the Controller
			// class
			user.getUserRoles().addAll(userRoles);
			
			// Finally save the user from the Controller...
			localUser = userRepository.save(user);
		}
		return localUser;
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}
}

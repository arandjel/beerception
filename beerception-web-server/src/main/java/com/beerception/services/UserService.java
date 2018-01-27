package com.beerception.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.beerception.entities.Role;
import com.beerception.entities.User;
import com.beerception.repository.RoleRepository;
import com.beerception.repository.UserRepository;

/**
 * Service class for managing user and user related configuration.
 * 
 * @author Miloš Ranđelović
 *
 */
@Service("userService")
public class UserService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * Gets user with matching username.
	 * 
	 * @param username User with passed username should be returned.
	 * @return User with matching username, or null if such is not found.
	 */
	public User findUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByEmail(username);
	}

	/**
	 * Gets user with matching email.
	 * 
	 * @param username User with passed email should be returned.
	 * @return User with matching email, or null if such is not found.
	 */
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	/**
	 * Gets user with matching id.
	 * 
	 * @param id User with passed id should be returned.
	 * @return User with matching id, or null if such is not found.
	 */
	public User findById(Integer id) throws AccessDeniedException {
		User u = userRepository.findOne(id);
		return u;
	}

	/**
	 * Gets all existing users.
	 * 
	 * @return List of all existing users.
	 */
	public List<User> findAll() throws AccessDeniedException {
		List<User> result = userRepository.findAll();
		return result;
	}

	/**
	 * Creates new user from provided User object.
	 * 
	 * @param user User object that should be added.
	 * 
	 * @return Added User object.
	 */
	public User addUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(true);
		// Role userRole = roleRepository.findByRole("ROLE_ADMIN");
		// user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		Role userRole = roleRepository.findByRole("ROLE_USER");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

		return userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else {
			return user;
		}
	}

	/**
	 * Change user's password.
	 * 
	 * @param oldPassword Old user's password.
	 * @param newPassword New user's password.
	 * @return <code>true</code> if password was changed successfully, or <code>false</code> otherwise.
	 */
	public boolean changePassword(String oldPassword, String newPassword) {
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		String username = currentUser.getName();

		if (authenticationManager != null) {
			logger.debug("Re-authenticating user '" + username + "' for password change request.");

			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
		} else {
			logger.debug("No authentication manager set. can't change Password!");

			return false;
		}

		logger.debug("Changing password for user '" + username + "'");

		User user = (User) loadUserByUsername(username);

		user.setPassword(bCryptPasswordEncoder.encode(newPassword));
		user = userRepository.save(user);
		
		if(user == null)
			return false;
		
		return true;
	}
}
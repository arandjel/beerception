package com.beerception.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.beerception.exception.ResourceConflictException;

import com.beerception.auth.TokenHelper;
import com.beerception.entities.User;
import com.beerception.model.UserTokenState;
import com.beerception.services.UserService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Value("${jwt.expires_in}")
	private int EXPIRES_IN;

	@Value("${jwt.cookie}")
	private String TOKEN_COOKIE;

	@Autowired
	private UserService userService;

	@Autowired
	private TokenHelper tokenHelper;

	static class PasswordChanger {
		public String oldPassword;
		public String newPassword;
	}

	/**
	 * Refresh user JWT token if token was was provided with the request, and token can be refreshed.
	 * Refreshed token is stored in the response cookie, and as response body message.
	 * 
	 * @param request Request to the server.
	 * @param response Response to the client.
	 * @return Refreshed token, or null token if token couldn't be refreshed.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/refresh")
	public ResponseEntity<?> refreshAuthenticationToken(HttpServletRequest request, HttpServletResponse response) {
		String authToken = tokenHelper.getToken(request);
		if (authToken != null && tokenHelper.canTokenBeRefreshed(authToken)) {
			String refreshedToken = tokenHelper.refreshToken(authToken);

			Cookie authCookie = new Cookie(TOKEN_COOKIE, (refreshedToken));
			authCookie.setPath("/");
			authCookie.setHttpOnly(true);
			authCookie.setMaxAge(EXPIRES_IN);

			response.addCookie(authCookie);

			UserTokenState userTokenState = new UserTokenState(refreshedToken, EXPIRES_IN);
			return ResponseEntity.ok(userTokenState);
		} else {
			UserTokenState userTokenState = new UserTokenState();
			return ResponseEntity.accepted().body(userTokenState);
		}
	}

	/**
	 * Change user's password.
	 * 
	 * @param passwordChanger Object containing old and new password.
	 * @return Information if password was changed with success or not.
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody PasswordChanger passwordChanger) {
		boolean success = userService.changePassword(passwordChanger.oldPassword, passwordChanger.newPassword);
		Map<String, String> result = new HashMap<>();
		
		if(success)
			result.put("result", "success");
		else
			result.put("result", "failed");
		
		return ResponseEntity.accepted().body(result);
	}

	/**
	 * Register new user.
	 * 
	 * @param userRequest User information for registration.
	 * @throws ResourceConflictException If username already exists.
	 * @throws MethodArgumentNotValidException If entity fields validation requirements are not met.
	 * @return User information generated by the application.
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/signup")
	public ResponseEntity<?> addUser(@Validated @RequestBody User userRequest) {

		User existUser = userService.findUserByUsername(userRequest.getUsername());
		if (existUser != null) {
			throw new ResourceConflictException(userRequest.getId(), "Username already exists");
		}
		User user = userService.addUser(userRequest);

		user.setPassword("");
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}

	/**
	 * Get information for this user from security context.
	 * 
	 * @return User information.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/whoami")
	public User getUser() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		user.setPassword("");
		
		return user;
	}
}

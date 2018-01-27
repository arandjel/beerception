package com.beerception.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.beerception.auth.AuthenticationFailureHandler;
import com.beerception.auth.AuthenticationSuccessHandler;
import com.beerception.auth.EntryPointUnauthorizedHandler;
import com.beerception.auth.LogoutSuccess;
import com.beerception.auth.TokenAuthenticationFilter;
import com.beerception.services.UserService;

/**
 * Class for providing security configuration details to Spring framework. 
 * 
 * @author Miloš Ranđelović
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private EntryPointUnauthorizedHandler unauthorizedHandler;
	 
	@Value("${spring.queries.users-query}")
	private String usersQuery;
	
	@Value("${spring.queries.roles-query}")
	private String rolesQuery;
	
	@Value("${jwt.cookie}")
	private String TOKEN_COOKIE;

	@Bean
	public TokenAuthenticationFilter jwtAuthenticationTokenFilter() throws Exception {
	  return new TokenAuthenticationFilter();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
	  return super.authenticationManagerBean();
	}

	@Autowired
	private UserService userService;
	
	@Autowired
	private LogoutSuccess logoutSuccess;

	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(userService)
			.passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)	throws Exception {
		auth
			.jdbcAuthentication()
				.usersByUsernameQuery(usersQuery)
				.authoritiesByUsernameQuery(rolesQuery)
				.dataSource(dataSource)
				.passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
	    	.csrf()
	        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
	   	.and()
	   		.cors()
	   	.and()
		    .sessionManagement()
		    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	   	.and()
			.exceptionHandling()
			.authenticationEntryPoint(this.unauthorizedHandler)
        .and()
           	.addFilterBefore(jwtAuthenticationTokenFilter(), BasicAuthenticationFilter.class)
       	.authorizeRequests()
			.antMatchers("/api/v1/auth/whoami", "/api/v1/auth/changePassword").hasAuthority("ROLE_USER")
			.antMatchers("/api/v1/auth/**").permitAll()
			.antMatchers(HttpMethod.GET, "/api/v1/beerception", "/api/v1/beerception/info").permitAll()
			.antMatchers("/api/v1/beerception/**").hasAnyAuthority("ROLE_USER")
			.antMatchers("/api/v1/admin/**").hasAuthority("ROLE_ADMIN")
			.antMatchers("/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
			.anyRequest()
			.authenticated()
		.and()
			.formLogin()
			.loginPage("/api/v1/auth/login")
			.usernameParameter("email")
			.passwordParameter("password")
	        .successHandler(authenticationSuccessHandler)
	        .failureHandler(authenticationFailureHandler)
		.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/api/v1/auth/logout"))
	        .logoutSuccessHandler(logoutSuccess).deleteCookies(TOKEN_COOKIE);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web
	       .ignoring()
	       .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/img/**", "/fonts/**");
	}
}
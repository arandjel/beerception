package com.beerception.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

/**
 * Class presenting Role entity. Class implements GrantedAuthority class for
 * providing role information to Spring Boot framework.
 * 
 * @author Miloš Ranđelović
 *
 */
@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 3833087132542693205L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "role_id")
	private Integer id;

	@Column(name = "role")
	private String role;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String getAuthority() {
		return role;
	}
}
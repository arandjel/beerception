package com.beerception.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Vote {

	@Id
	@GeneratedValue
	private int id;
	
	private int beerId;
	
	private int userId;
	
	private int vote;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBeerId() {
		return beerId;
	}

	public void setBeerId(int beerId) {
		this.beerId = beerId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getVote() {
		return vote;
	}

	public void setVote(int vote) {
		this.vote = vote;
	}
}

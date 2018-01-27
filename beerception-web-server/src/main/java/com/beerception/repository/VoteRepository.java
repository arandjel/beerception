package com.beerception.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beerception.entities.Vote;

public interface VoteRepository extends JpaRepository<Vote, Integer>{
	
    public Vote findByBeerIdAndUserId(Integer beerId, Integer userId);
}

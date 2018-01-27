package com.beerception.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.beerception.entities.BeerFile;

public interface BeerFileRepository extends JpaRepository<BeerFile, Integer>{

	@Query(value = "SELECT * from beer_file ORDER BY id DESC LIMIT 1", nativeQuery = true)
    public BeerFile findLatestBeerFile();
}

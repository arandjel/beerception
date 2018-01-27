package com.beerception.services;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.beerception.entities.BeerFile;
import com.beerception.entities.User;
import com.beerception.entities.Vote;
import com.beerception.repository.BeerFileRepository;
import com.beerception.repository.VoteRepository;

@Service
public class BeerFileService {

	private static final Logger logger = LoggerFactory.getLogger(BeerFileService.class);

	@Autowired
	private BeerFileRepository beerFileRepository;
	
	@Autowired
	private VoteRepository voteRepository;
	
	public BeerFile addBeerFile(MultipartFile multiPartFile) {
		BeerFile beerFile = new BeerFile();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			beerFile.setImage(multiPartFile.getBytes());
			beerFile.setBeerDate(new Date());
			beerFile.setImageName(multiPartFile.getOriginalFilename());
			beerFile.setOwner(auth.getName());
			
			beerFile = beerFileRepository.save(beerFile);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		return beerFile;
	}
	
	public BeerFile getLatestBeerFile() {
		return beerFileRepository.findLatestBeerFile();
	}

	public synchronized BeerFile beerVote(int vote) {
		if(vote == 1)
			return beerUp();
		else
			return beerDown();
	}
	
	public BeerFile beerUp() {
		BeerFile beerFile = this.getLatestBeerFile();
		
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Vote vote = voteRepository.findByBeerIdAndUserId(beerFile.getId(), user.getId());
		
		if (vote == null)
		{	
			vote = new Vote();
			vote.setBeerId(beerFile.getId());
			vote.setUserId(user.getId());
		}
		
		if (vote.getVote() != 1) {
			if(vote.getVote() == -1)
				beerFile.setBeerDown(beerFile.getBeerDown() - 1);
			
			beerFile.setBeerUp(beerFile.getBeerUp() + 1);
			beerFile = beerFileRepository.save(beerFile);
			vote.setVote(1);
			voteRepository.save(vote);
		}
		
		return beerFile;
	}

	public BeerFile beerDown() {
		BeerFile beerFile = this.getLatestBeerFile();
		
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Vote vote = voteRepository.findByBeerIdAndUserId(beerFile.getId(), user.getId());
		
		if (vote == null)
		{	
			vote = new Vote();
			vote.setBeerId(beerFile.getId());
			vote.setUserId(user.getId());
		}
		
		if (vote.getVote() != -1) {
			if(vote.getVote() == 1)
				beerFile.setBeerUp(beerFile.getBeerUp() - 1);
			
			beerFile.setBeerDown(beerFile.getBeerDown() + 1);
			beerFile = beerFileRepository.save(beerFile);
			vote.setVote(-1);
			voteRepository.save(vote);
			
			if (beerFile.getBeerUp() - beerFile.getBeerDown() < -10)
			{	
				beerFileRepository.delete(beerFile);
				return null;
			}
		}
		
		return beerFile;
	}
}

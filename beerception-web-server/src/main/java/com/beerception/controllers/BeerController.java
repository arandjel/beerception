package com.beerception.controllers;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import com.beerception.entities.BeerFile;
import com.beerception.exception.DoubleUploadException;
import com.beerception.services.BeerFileService;

@RestController
@RequestMapping("/api/v1/beerception")
public class BeerController {

	private static final Logger logger = LoggerFactory.getLogger(BeerController.class);

	@Autowired
	private BeerFileService beerFileService;
	
	@RequestMapping(method=RequestMethod.POST, value="")
	public ResponseEntity<?> addBeerFile(@RequestParam("beerFile") MultipartFile beerFile) {
		if (beerFile == null)
			return new ResponseEntity<>("Not a valid request!", HttpStatus.BAD_REQUEST);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth.getName().equals(beerFileService.getLatestBeerFile().getOwner()))
			throw new DoubleUploadException("You cannot upload photo again before someone else does!");
		
		BeerFile newBeerFile = beerFileService.addBeerFile(beerFile);
		
		return new ResponseEntity<>(newBeerFile, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="")
	public ResponseEntity<?> getLatestBeerFile() {
		BeerFile beerFile = beerFileService.getLatestBeerFile();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + beerFile.getImageName() + "\"");
		headers.add(HttpHeaders.CONTENT_TYPE, "image/jpeg");
		
		byte[] encoded = Base64.getEncoder().encode(beerFile.getImage());
		
		return new ResponseEntity<byte[]>(encoded, headers, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/info")
	public ResponseEntity<?> getLatestBeerFileInfo() {
		BeerFile beerFile = beerFileService.getLatestBeerFile();
				
		return new ResponseEntity<>(beerFile, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/beerup")
	public ResponseEntity<?> beerUp() {
		BeerFile beerFile = beerFileService.beerVote(1);
		
		return new ResponseEntity<>(beerFile, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/beerdown")
	public ResponseEntity<?> beerDown() {
		BeerFile beerFile = beerFileService.beerVote(-1);
		
		if (beerFile == null)
			return new ResponseEntity<>("Too many beer downs... Beer file got deleted! :(", HttpStatus.OK);
			
		return new ResponseEntity<>(beerFile, HttpStatus.OK);
	}
	
	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Current request is not a multipart request. Request must contain key 'beerFile' bound to file to be uploaded.")
	@ExceptionHandler({MultipartException.class})
	public void idParameterError(Exception ex) {
		logger.error(ex.getMessage());
	}
}

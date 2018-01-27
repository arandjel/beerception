package com.beerception.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class BeerFile {

	@Id
	@GeneratedValue
	private int id;
	
	private String owner;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date beerDate;
	
	private int beerUp;
	
	private int beerDown;
	
	@JsonIgnore
	@Lob
	private byte[] image;
	
	private String imageName;
	
	public BeerFile() {
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Date getBeerDate() {
		return beerDate;
	}

	public void setBeerDate(Date beerDate) {
		this.beerDate = beerDate;
	}

	public int getBeerUp() {
		return beerUp;
	}

	public void setBeerUp(int beerUp) {
		this.beerUp = beerUp;
	}

	public int getBeerDown() {
		return beerDown;
	}

	public void setBeerDown(int beerDown) {
		this.beerDown = beerDown;
	}
 
    public byte[] getImage() {
        return image;
    }
 
    public void setImage(byte[] image) {
        this.image = image;
    }

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
}

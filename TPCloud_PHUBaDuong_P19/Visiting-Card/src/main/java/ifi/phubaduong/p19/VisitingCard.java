package ifi.phubaduong.p19;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class VisitingCard implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	@Index private String firstName;
	@Index private String lastName;
	@Index private String userName;
	@Index private String telNumber;
	@Index private String email;
	private String organisation;
	private String position;
	private Date createdDate;

	public VisitingCard() {
		super();
	}

	public VisitingCard(String firstName, String lastName, String telNumber, String email,
			String organisation, String position) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.telNumber = telNumber;
		this.email = email;
		this.organisation = organisation;
		this.position = position;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

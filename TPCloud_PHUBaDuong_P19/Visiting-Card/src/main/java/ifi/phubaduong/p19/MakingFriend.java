package ifi.phubaduong.p19;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class MakingFriend implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	@Index
	private String userName;
	@Index
	private String friendName;
	@Index
	private String status;
	private Date createdDate;
	private Date acceptedDate;

	public MakingFriend() {
		super();
	}

	public MakingFriend(String userName, String friendName, String status) {
		super();
		this.userName = userName;
		this.friendName = friendName;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		createdDate = createdDate;
	}

	public Date getAcceptedDate() {
		return acceptedDate;
	}

	public void setAcceptedDate(Date acceptedDate) {
		acceptedDate = acceptedDate;
	}
}

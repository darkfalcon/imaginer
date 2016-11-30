package hu.neuron.imaginer.user.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String username;

	private String firstName;

	private String lastName;

	private String emailAddress;

	private Date birthDate;
	
	private boolean activated;
	
	private boolean disabled;
	
	private List<UserGroupVO> userGroups;

	public UserVO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public List<UserGroupVO> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<UserGroupVO> userGroups) {
		this.userGroups = userGroups;
	}

	@Override
	public String toString() {
		return "UserVO [id=" + id + ", username=" + username + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", emailAddress=" + emailAddress + ", birthDate=" + birthDate + ", activated=" + activated
				+ ", disabled=" + disabled + ", userGroups=" + userGroups + "]";
	}
}

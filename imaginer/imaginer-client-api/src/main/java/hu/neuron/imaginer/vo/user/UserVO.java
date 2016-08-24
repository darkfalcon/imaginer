package hu.neuron.imaginer.vo.user;

import java.io.Serializable;
import java.util.Date;

public class UserVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private String username;

	private String firstName;

	private String lastName;

	private String emailAddress;

	private Date birthDate;

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

	@Override
	public String toString() {
		return "UserVO [id=" + id + ", username=" + username + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", emailAddress=" + emailAddress + ", birthDate=" + birthDate + "]";
	}
}

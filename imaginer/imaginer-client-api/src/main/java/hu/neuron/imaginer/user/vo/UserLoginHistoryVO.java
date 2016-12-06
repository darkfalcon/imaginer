package hu.neuron.imaginer.user.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserLoginHistoryVO {

	private String emailAddress;
	private List<Date> loginDates;

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public List<Date> getLoginDates() {
		if (this.loginDates == null) {
			this.loginDates = new ArrayList<>();
		}
		return loginDates;
	}

	public void setLoginDates(List<Date> loginDates) {
		this.loginDates = loginDates;
	}

}

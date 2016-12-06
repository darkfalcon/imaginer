package hu.neuron.imaginer.report;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.omnifaces.util.Faces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import hu.neuron.imaginer.exception.ApplicationException;
import hu.neuron.imaginer.user.service.UserService;
import hu.neuron.imaginer.user.vo.UserLoginHistoryVO;
import hu.neuron.imaginer.util.CSVUtil;
import hu.neuron.imaginer.util.MessageBundle;

@ViewScoped
@ManagedBean
public class ReportManagedBean {

	private static final Logger logger = LoggerFactory.getLogger(ReportManagedBean.class.getName());

	@ManagedProperty("#{userServiceImpl}")
	UserService userService;

	private static final DateFormat RECORD_FORMATTER = new SimpleDateFormat("yyyy.MM.dd HH:mm");
	private static final DateFormat FILE_NAME_FORMATTER = new SimpleDateFormat("yyyy.MM.dd-HH:mm");

	private String username;

	public String createReport() {
		try {
			List<UserLoginHistoryVO> result = new ArrayList<>();
			if (!StringUtils.isEmpty(this.username)) {
				result.add(userService.getUserLoginHistory(this.username));
			} else {
				result.addAll(userService.getUsersLoginHistory());
			}
			StringBuilder name = new StringBuilder("report")
					.append(StringUtils.isEmpty(this.username) ? "_all_" : '_' + this.username + '_')
					.append(FILE_NAME_FORMATTER.format(new Date())).append(".csv");
			File file = new File(name.toString());
			FileWriter writer = new FileWriter(file);

			for (UserLoginHistoryVO userLoginHistoryVO : result) {
				for (Date loginDate : userLoginHistoryVO.getLoginDates()) {
					CSVUtil.writeLine(writer,
							Arrays.asList(userLoginHistoryVO.getEmailAddress(), RECORD_FORMATTER.format(loginDate)));
				}
			}
			writer.flush();
			Faces.sendFile(file, true);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					MessageFormat.format(MessageBundle.getBundle("imaginer.report.successful"), name.toString())));
		} catch (ApplicationException e) {
			logger.error(e.getMessage(), e);
			String message = MessageBundle.getBundle("imaginer.report.error") + " " + MessageFormat.format(
					MessageBundle.getBundle("imaginer.error.service." + e.getErrorType().getAsString()), this.username);
			FacesMessage facesMessage = new FacesMessage(message);
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			String message = MessageBundle.getBundle("imaginer.report.error");
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
		this.username = null;
		return null;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}

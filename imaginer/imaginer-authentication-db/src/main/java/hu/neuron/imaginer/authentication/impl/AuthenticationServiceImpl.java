package hu.neuron.imaginer.authentication.impl;

import java.text.MessageFormat;
import java.util.HashMap;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jndi.JndiTemplate;

import hu.neuron.imaginer.authentication.AuthenticationService;
import hu.neuron.imaginer.authentication.request.AuthenticationRequest;
import hu.neuron.imaginer.authentication.response.AuthenticationResponse;
import hu.neuron.imaginer.authentication.response.AuthenticationResponse.AuthenticationResult;
import hu.neuron.imaginer.exception.ApplicationException;
import hu.neuron.imaginer.exception.ErrorType;

@PropertySource(value = "file:app-config/database-config.properties", ignoreResourceNotFound = false)
public class AuthenticationServiceImpl implements AuthenticationService {

	@Value("${jdbc.datasource.jndi}")
	String datasourceJndi;

	public AuthenticationResponse authenticate(final AuthenticationRequest request) {
		try {
			JndiTemplate jndiTemplate = new JndiTemplate();
			DataSource dataSource;

			dataSource = (DataSource) jndiTemplate.lookup("java:comp/env/jdbc/UsersDB");

			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

			String query = "SELECT 1 FROM user WHERE username = :username AND password = :password";

			Integer result = jdbcTemplate.queryForObject(query, new HashMap<String, Object>() {
				{
					put("username", request.getUsername());
					put("password", request.getPassword());
				}
			}, Integer.class);

			if (result != null && !result.equals(1)) {
				return new AuthenticationResponse(AuthenticationResult.FAILED);
			} else {
				return new AuthenticationResponse(AuthenticationResult.SUCCESS);
			}
		} catch (NamingException e) {
			throw new ApplicationException(ErrorType.AUTHENTICATION_ERROR,
					MessageFormat.format("Failed to authenticate user[{0}]", request.getUsername()), e);
		}
	}

}

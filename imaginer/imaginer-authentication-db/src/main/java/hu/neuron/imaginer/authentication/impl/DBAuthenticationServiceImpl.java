package hu.neuron.imaginer.authentication.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import hu.neuron.imaginer.authentication.AuthenticationService;
import hu.neuron.imaginer.authentication.request.AuthenticationRequest;
import hu.neuron.imaginer.authentication.response.AuthenticationResponse;
import hu.neuron.imaginer.authentication.response.AuthenticationResponse.AuthenticationResult;

@PropertySource(value = "file:app-config/database-config.properties", ignoreResourceNotFound = false)
@Service
public class DBAuthenticationServiceImpl implements AuthenticationService {

	@Value("${jdbc.datasource.jndi}")
	String datasourceJndi;

	public AuthenticationResponse authenticate(final AuthenticationRequest request) {
		DataSource dataSource = new JndiDataSourceLookup().getDataSource(datasourceJndi);

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

		String query = "SELECT password, activated FROM user WHERE username = :username";

		Map<String, Object> paramMap = new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;

			{
				put("username", request.getUsername());
			}
		};

		List<UserAuthenticationData> result = jdbcTemplate.query(query, paramMap, new RowMapper<UserAuthenticationData>() {

			public UserAuthenticationData mapRow(ResultSet rs, int arg) throws SQLException {
				return new UserAuthenticationData(rs.getString(1), rs.getBoolean(2));
			}
		});

		if (result == null || result.size() != 1) {
			return new AuthenticationResponse(AuthenticationResult.USER_NOT_FOUND);
		} else {
			UserAuthenticationData autData = result.get(0);
			if (BCrypt.checkpw(request.getPassword(), autData.getPassword())) {
				if (autData.getActivated()) {
					return new AuthenticationResponse(AuthenticationResult.SUCCESS);
				} else {
					return new AuthenticationResponse(AuthenticationResult.NOT_ACTIVATED);
				}
			} else {
				return new AuthenticationResponse(AuthenticationResult.INVALID_PASSWORD);
			}
		}
	}

	private static class UserAuthenticationData {
		private String password;
		private Boolean activated;

		public UserAuthenticationData(String password, Boolean activated) {
			super();
			this.password = password;
			this.activated = activated;
		}

		public String getPassword() {
			return password;
		}

		public Boolean getActivated() {
			return activated;
		}
	}
}

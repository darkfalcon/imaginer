package hu.neuron.imaginer.authentication.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.stereotype.Service;

import hu.neuron.imaginer.authentication.AuthenticationService;
import hu.neuron.imaginer.authentication.request.AuthenticationRequest;
import hu.neuron.imaginer.authentication.response.AuthenticationResponse;
import hu.neuron.imaginer.authentication.response.AuthenticationResponse.AuthenticationResult;

@PropertySource(value = "file:app-config/database-config.properties", ignoreResourceNotFound = false)
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Value("${jdbc.datasource.jndi}")
	String datasourceJndi;

	public AuthenticationResponse authenticate(final AuthenticationRequest request) {
		DataSource dataSource = new JndiDataSourceLookup().getDataSource(datasourceJndi);

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

		String query = "SELECT 1 FROM user WHERE username = :username AND password = :password";

		Map<String, Object> paramMap = new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("username", request.getUsername());
				put("password", request.getPassword());
			}
		};
		
		List<Integer> result = jdbcTemplate.query(query, paramMap, new RowMapper<Integer>() {

			public Integer mapRow(ResultSet rs, int arg) throws SQLException {
				return rs.getInt(1);
			}
			
		});

		if (result == null || result.size() != 1) {
			return new AuthenticationResponse(AuthenticationResult.FAILED);
		} else {
			return new AuthenticationResponse(AuthenticationResult.SUCCESS);
		}
	}

}

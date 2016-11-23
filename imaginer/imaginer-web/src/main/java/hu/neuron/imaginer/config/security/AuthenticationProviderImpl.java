package hu.neuron.imaginer.config.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import hu.neuron.imaginer.authentication.AuthenticationService;
import hu.neuron.imaginer.authentication.request.AuthenticationRequest;
import hu.neuron.imaginer.authentication.response.AuthenticationResponse;
import hu.neuron.imaginer.authentication.response.AuthenticationResponse.AuthenticationResult;
import hu.neuron.imaginer.service.UserService;

@Service
public class AuthenticationProviderImpl implements AuthenticationProvider {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private UserService userService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		AuthenticationResponse authenticationResponse = authenticationService
				.authenticate(new AuthenticationRequest(username, password));
		if (AuthenticationResult.SUCCESS.equals(authenticationResponse.getResult())) {
			return new UsernamePasswordAuthenticationToken(authentication.getName(), password,
					getAuthorities(username));
		} else {
			throw new BadCredentialsException("Incorrect credentials for user: " + username);
		}
	}

	private List<GrantedAuthority> getAuthorities(String username) {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
		grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
		return grantedAuthorities;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}

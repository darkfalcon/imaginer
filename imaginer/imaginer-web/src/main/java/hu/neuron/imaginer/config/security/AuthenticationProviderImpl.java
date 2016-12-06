package hu.neuron.imaginer.config.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hu.neuron.imaginer.authentication.AuthenticationService;
import hu.neuron.imaginer.authentication.request.AuthenticationRequest;
import hu.neuron.imaginer.authentication.response.AuthenticationResponse;
import hu.neuron.imaginer.user.service.UserService;
import hu.neuron.imaginer.user.vo.UserGroupVO;

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
		switch (authenticationResponse.getResult()) {
		case SUCCESS:
			createLoginHistoryEntry(username);
			return new UsernamePasswordAuthenticationToken(authentication.getName(), password,
					getAuthorities(username));
		case USER_NOT_FOUND:
			throw new UsernameNotFoundException("There is no such user registered: " + username);
		case INVALID_PASSWORD:
			throw new BadCredentialsException("Incorrect credentials for user: " + username);
		case NOT_ACTIVATED:
			throw new DisabledException("Account is not activated");
		default:
			throw new AuthenticationServiceException("");
		}
	}

	private List<GrantedAuthority> getAuthorities(String username) {
		List<UserGroupVO> userGroups = userService.getUserGroupsForUser(username);
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		for (UserGroupVO userGroupVO : userGroups) {
			grantedAuthorities.add(new SimpleGrantedAuthority(userGroupVO.getCode()));
		}
		return grantedAuthorities;
	}
	
	private void createLoginHistoryEntry(String username) {
		userService.createLoginHistoryEntry(username);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}

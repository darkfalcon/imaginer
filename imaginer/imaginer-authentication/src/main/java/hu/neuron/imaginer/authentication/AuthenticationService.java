package hu.neuron.imaginer.authentication;

import hu.neuron.imaginer.authentication.request.AuthenticationRequest;
import hu.neuron.imaginer.authentication.response.AuthenticationResponse;

public interface AuthenticationService {
	
	public AuthenticationResponse authenticate(AuthenticationRequest request);

}

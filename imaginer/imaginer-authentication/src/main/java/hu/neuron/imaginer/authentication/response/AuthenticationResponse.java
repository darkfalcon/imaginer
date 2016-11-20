package hu.neuron.imaginer.authentication.response;

public class AuthenticationResponse {
	
	private AuthenticationResult result;
	
	public AuthenticationResponse(AuthenticationResult result) {
		super();
		this.result = result;
	}

	public AuthenticationResult getResult() {
		return result;
	}

	public static enum AuthenticationResult {
		SUCCESS, FAILED
	}
}

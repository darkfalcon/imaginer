package hu.neuron.imaginer.config.security;

import org.springframework.security.web.context.*;

public class SecurityInitializer
      extends AbstractSecurityWebApplicationInitializer {

    public SecurityInitializer() {
        super(SecurityConfiguration.class);
    }
}
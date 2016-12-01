package hu.neuron.imaginer.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .logoutSuccessUrl(
                        "/login.xhtml");

        http.authorizeRequests()
                .antMatchers("/public/**")
                .permitAll()
                .antMatchers("/secured/**")
                .hasAnyAuthority("USER")
                .antMatchers("/report/**")
                .hasAnyAuthority("REPORT")
                .and()
                .formLogin()
                .loginPage("/public/login.xhtml")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/secured/index.xhtml")
                .and().exceptionHandling().accessDeniedPage("/public/error.xhtml");
    }
    
    @Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth, AuthenticationProviderImpl authenticationProvider) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}
}
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

        // Have to disable it for POST methods:
        // http://stackoverflow.com/a/20608149/1199132
        http.csrf().disable();

        // Logout and redirection:
        // http://stackoverflow.com/a/24987207/1199132
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .logoutSuccessUrl(
                        "/login.xhtml");

        http.authorizeRequests()
                // Some filters enabling url regex:
                // http://stackoverflow.com/a/8911284/1199132
                .antMatchers("/public/**")
                .permitAll()
                //Permit access for all to error and denied views
                // Only access with admin role
                .antMatchers("/secured/**")
                .hasAnyRole("USER", "ADMIN", "MANAGER")
                //If user doesn't have permission, forward him to login page
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

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth)
//            throws Exception {
//        //Configure roles and passwords as in-memory authentication
//        auth.inMemoryAuthentication()
//                .withUser("administrator")
//                .password("pass")
//                .roles("ADMIN");
//        auth.inMemoryAuthentication()
//                .withUser("manager")
//                .password("pass")
//                .roles("MANAGEMENT");
//        auth.inMemoryAuthentication()
//        .withUser("user")
//        .password("pass")
//        .roles("USER");
//    }
}
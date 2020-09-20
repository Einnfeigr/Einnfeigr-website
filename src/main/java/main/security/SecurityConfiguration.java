package main.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	 @Override
	protected void configure(AuthenticationManagerBuilder auth)
			 throws Exception {
	  	PasswordEncoder encoder = 
	    PasswordEncoderFactories.createDelegatingPasswordEncoder();
	  	auth
	  		.inMemoryAuthentication()
	        .withUser("studiedlist")
	        .password(encoder.encode(System.getenv("adminPassword")))
	        .roles("USER", "ADMIN"); 
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web
			.ignoring()
	        .antMatchers("/resources/**");
	}
	    
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.formLogin()
			.loginPage("/login")
			.loginProcessingUrl("/login")
			.defaultSuccessUrl("/dashboard")
			.failureUrl("/loginFailed")
			.and()
			.logout()
			.logoutUrl("/logout")
			.and()
			.authorizeRequests()
			.antMatchers("/dashboard").hasRole("USER")
			.antMatchers("/dashboard/*").hasRole("USER")
			.anyRequest().permitAll()
			.and()
			.httpBasic();
	}
	
}

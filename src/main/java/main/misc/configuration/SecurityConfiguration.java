package main.misc.configuration;

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
	  		.withUser("user")
	  		.password(encoder.encode("password"))
	        .roles("USER")
	        .and()
	        .withUser("admin")
	        .password(encoder.encode("admin"))
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
	      .authorizeRequests()
	      .antMatchers("/dashboard*").authenticated()
	      .anyRequest().permitAll()
	      .and()
	      .formLogin()
	      .loginPage("/login")
	      .loginProcessingUrl("/performLogin")
	      .defaultSuccessUrl("/dashboard", true)
	      .and()
	      .httpBasic();
	}
	
}

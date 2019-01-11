package it.olegna.template.application.auth.svr.configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

    @Autowired
    private PasswordEncoder delPwdEnc;
    @Autowired
    private UserDetailsService oauthUsrDetailSvc;

	@Autowired
	public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception
	{
		auth
		.userDetailsService(oauthUsrDetailSvc)
		.passwordEncoder(delPwdEnc);
	}
	@Override
	@Bean
	@Primary
	public AuthenticationManager authenticationManagerBean() throws Exception
	{
		return super.authenticationManagerBean();
	}
	@Override
	protected void configure(final HttpSecurity http) throws Exception
	{
		http
		.authorizeRequests()
			.antMatchers("/login").permitAll()
			.antMatchers("/oauth/token/revokeById/**")
			.permitAll()
			.antMatchers("/oauth/tokens/**", "/errors/**")
			.permitAll()
			.anyRequest()
			.authenticated()
		.and()
			.formLogin()
			.permitAll()
		.and()
			.csrf()
			.disable()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.NEVER)
		;
	}
	
}
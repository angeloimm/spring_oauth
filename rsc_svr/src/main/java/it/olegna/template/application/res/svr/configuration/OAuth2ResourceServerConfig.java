package it.olegna.template.application.res.svr.configuration;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.DelegatingJwtClaimsSetVerifier;
import org.springframework.security.oauth2.provider.token.store.IssuerClaimVerifier;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtClaimsSetVerifier;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import it.olegna.template.application.configuration.DbConfig;
import it.olegna.template.application.res.svr.configuration.util.CustomClaimVerifier;

@Configuration
@EnableWebSecurity
@EnableResourceServer
@PropertySource(value= {"classpath:configuration.properties"}, encoding="UTF-8", ignoreResourceNotFound=false)
@Import(value= {ResourceServerWebConfig.class, MethodSecurityConfig.class, DbConfig.class})
@ComponentScan(basePackages= {"it.olegna.template.application.resource.server"})
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(OAuth2ResourceServerConfig.class.getName());
	@Autowired
	private Environment env;
	@Override
	public void configure(final HttpSecurity http) throws Exception {
		http.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.NEVER)      
		.and()
		.authorizeRequests()
		.anyRequest()
		.authenticated()          	
		.and()
		.exceptionHandling()
		.accessDeniedHandler(new OAuth2AccessDeniedHandler())
		.and()
		.cors()
		.configurationSource(corsConfigurationSource());
	}
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		boolean abilitaCors = new Boolean(env.getProperty("templating.oauth.enable.cors"));
		if( abilitaCors )
		{
			if( logger.isWarnEnabled() )
			{
				logger.warn("CORS ABILITATI! Si assume ambiente di sviluppo");
			}
			CorsConfiguration configuration = new CorsConfiguration();
			configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200","http://localhost:8080"));
			configuration.setAllowedMethods(Arrays.asList("GET","POST", "OPTIONS"));
			configuration.setAllowedHeaders(Arrays.asList("x-requested-with"));
			source.registerCorsConfiguration("/**", configuration);
		}
		return source;
	}
	@Override
	public void configure(final ResourceServerSecurityConfigurer config) {
		config
		.tokenServices(tokenServices())
		.resourceId("resource_server");
	}

	// JWT
	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		final DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(tokenStore());
		return tokenServices;
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(env.getProperty("templating.oauth.token.encription.secure.string"));
		converter.setJwtClaimsSetVerifier(jwtClaimsSetVerifier());
		return converter;
	}

	@Bean
	public JwtClaimsSetVerifier jwtClaimsSetVerifier() {
		return new DelegatingJwtClaimsSetVerifier(Arrays.asList(issuerClaimVerifier(), customJwtClaimVerifier()));
	}

	@Bean
	public JwtClaimsSetVerifier issuerClaimVerifier() {
		try {
			return new IssuerClaimVerifier(new URL("http://localhost:8080"));
		} catch (final MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	@Bean
	public JwtClaimsSetVerifier customJwtClaimVerifier() {
		return new CustomClaimVerifier();
	}
}
package it.olegna.template.application.auth.svr.configuration;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import it.olegna.template.application.auth.svr.configuration.util.CustomJwtAccessTokenConverter;
import it.olegna.template.application.auth.svr.configuration.util.CustomTokenEnhancer;
import it.olegna.template.application.configuration.DbConfig;

@Configuration
@EnableAuthorizationServer
@PropertySource(value= {"classpath:configuration.properties"}, encoding="UTF-8", ignoreResourceNotFound=false)
@ComponentScan(basePackages= {"it.olegna.template.application.authorization.server"})
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import(value= {WebSecurityConfig.class, WebMvcConfig.class, DbConfig.class})
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter
{
	@Autowired
	private Environment env;
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder delPwdEnc;
    @Autowired
    private UserDetailsService oauthUsrDetailSvc;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
        	.tokenKeyAccess("permitAll()")
        	.checkTokenAccess("isAuthenticated()")
        	.passwordEncoder(delPwdEnc)
        	.accessDeniedHandler(oauthAccessDeniedHandler());
    }
    @Bean
    public OAuth2AccessDeniedHandler oauthAccessDeniedHandler() {
        return new OAuth2AccessDeniedHandler();
    }
    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
    	clients.withClientDetails(clientDetailsService);
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
        endpoints
        	.tokenStore(tokenStore())
        	.tokenEnhancer(tokenEnhancerChain)
        	.authenticationManager(authenticationManager)
        	.userDetailsService(oauthUsrDetailSvc);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final CustomJwtAccessTokenConverter converter = new CustomJwtAccessTokenConverter();
        converter.setSigningKey(env.getProperty("templating.oauth.token.encription.secure.string"));
        // final KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("mytest.jks"), "mypass".toCharArray());
        // converter.setKeyPair(keyStoreKeyFactory.getKeyPair("mytest"));
        return converter;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }
}
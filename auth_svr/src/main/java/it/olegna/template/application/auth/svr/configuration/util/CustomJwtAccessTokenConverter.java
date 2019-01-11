package it.olegna.template.application.auth.svr.configuration.util;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
		OAuth2Authentication authentication = super.extractAuthentication(map);
		Authentication userAuthentication = authentication.getUserAuthentication();

		if (userAuthentication != null) {
			LinkedHashMap userDetails = (LinkedHashMap) map.get("userDetails");
			if (userDetails != null) {

				// build your principal here
				String localUserTableField = (String) userDetails.get("localUserTableField");
//                ClientDetails extendedPrincipal = new CustomUserDetails(localUserTableField);
//
//                Collection<? extends GrantedAuthority> authorities = userAuthentication.getAuthorities();

//                userAuthentication = new UsernamePasswordAuthenticationToken(extendedPrincipal,
//                        userAuthentication.getCredentials(), authorities);
			}
		}
		return new OAuth2Authentication(authentication.getOAuth2Request(), userAuthentication);

	}

}
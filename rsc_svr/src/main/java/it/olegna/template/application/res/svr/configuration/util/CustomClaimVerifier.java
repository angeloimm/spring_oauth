package it.olegna.template.application.res.svr.configuration.util;
import java.util.List;
import java.util.Map;

import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.store.JwtClaimsSetVerifier;

public class CustomClaimVerifier implements JwtClaimsSetVerifier
{

	@SuppressWarnings("unchecked")
	@Override
	public void verify(Map<String, Object> claims) throws InvalidTokenException
	{
		if( !claims.containsKey("aud") ){
			throw new InvalidTokenException("Nessun resource ID passato");
		}
		else {
			final List<String> resourcesId = (List<String>) claims.get("aud");
			if (resourcesId.isEmpty())
			{
				throw new InvalidTokenException("Passato resources ID vuoti");
			}else if( !resourcesId.contains("resource_server") )
			{
				throw new InvalidTokenException("Passato dei resources ID non corretti "+resourcesId+"");
			}
		}
	}

}
package it.olegna.template.application.commons.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;

import it.olegna.template.application.commons.exception.CommonDbException;
import it.olegna.template.application.commons.persistence.models.OauthClientDetails;
import it.olegna.template.application.commons.services.OauthClientDetailSvc;

@Service
public class OauthClientSvcImpl implements ClientDetailsService, ClientRegistrationService {
	private static final Logger logger = LoggerFactory.getLogger(OauthClientSvcImpl.class.getName());
	@Autowired
	private OauthClientDetailSvc svc;
	@Override
	public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
		
		if( logger.isWarnEnabled() )
		{
			logger.warn("METODO DA IMPLEMENTARE");
		}
		throw new ClientAlreadyExistsException("METODO addClientDetails NON SUPPORTATO");
	}

	@Override
	public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
		if( logger.isWarnEnabled() )
		{
			logger.warn("METODO DA IMPLEMENTARE");
		}
		throw new NoSuchClientException("METODO updateClientDetails NON SUPPORTATO");
	}

	@Override
	public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
		if( logger.isWarnEnabled() )
		{
			logger.warn("METODO DA IMPLEMENTARE");
		}
		throw new NoSuchClientException("METODO updateClientSecret NON SUPPORTATO");
	}

	@Override
	public void removeClientDetails(String clientId) throws NoSuchClientException {
		if( logger.isWarnEnabled() )
		{
			logger.warn("METODO DA IMPLEMENTARE");
		}
		throw new NoSuchClientException("METODO removeClientDetails NON SUPPORTATO");

	}

	@Override
	public List<ClientDetails> listClientDetails() {
		if( logger.isWarnEnabled() )
		{
			logger.warn("METODO DA IMPLEMENTARE");
		}
		throw new NoSuchClientException("METODO removeClientDetails NON SUPPORTATO");
	}

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		try {
			ClientDetails result = null;
			OauthClientDetails ocd = svc.findByClientId(clientId);
			if( ocd == null )
			{
				if( logger.isInfoEnabled() )
				{
					logger.info("Nessun client trovato con ID {}", clientId);
				}
			}else{
				result = ocd.toClientDetails();	
			
				if( logger.isDebugEnabled() )
				{
					StringBuilder sb = new StringBuilder();
					sb.append("CLIENT ID [");
					sb.append(result.getClientId());
					sb.append("] ");
					sb.append("ACCESS TOKEN VALIDITY SECONDS [");
					sb.append(result.getAccessTokenValiditySeconds());
					sb.append("]");
					logger.debug("Trovato client detail {}", sb.toString());
				}
			}
			return result;
		} catch (CommonDbException e) {
			String msg = "Errore nella ricerca del client con ID "+clientId;
			logger.error(msg, e);
			throw new ClientRegistrationException(msg, e);
		}
	}

}

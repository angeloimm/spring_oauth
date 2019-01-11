package it.olegna.template.application.commons.listeners;

import java.util.Collections;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import it.olegna.template.application.commons.exception.CommonDbException;
import it.olegna.template.application.commons.persistence.models.OauthClientDetails;
import it.olegna.template.application.commons.persistence.models.OauthClientGrant;
import it.olegna.template.application.commons.services.OauthClientDetailSvc;
import it.olegna.template.application.commons.services.OauthClientGrantSvc;

@Component
public class CtxListener {
	
	private static final Logger logger = LoggerFactory.getLogger(CtxListener.class.getName());
	@Autowired
	private OauthClientDetailSvc clientDetails;
	@Autowired
	private OauthClientGrantSvc grantSvc;
	
	@EventListener
	public void ctxLoadedListener( ContextRefreshedEvent evt )
	{
		try {
			Long grants = grantSvc.contaOauthClientGrant();
			if( grants == 0 )
			{
				OauthClientGrant grant = creaGrant();
				this.grantSvc.salvaOauthClientGrant(grant);
				creaGrantDefault(grant);
			}
		} catch (Exception e) {
			logger.error("Errore inizializzazione applicativo", e);
		}
	}
	private OauthClientGrant creaGrant()
	{
		if( logger.isDebugEnabled() )
		{
			logger.debug("Nessun grant salvato; creazione grant di default"); 
		}
		OauthClientGrant result = new OauthClientGrant();
		result.setCreatoDa("LISTENER");
		result.setDataCreazione(new Date());
		result.setName("DEF_AUTH_GRANT");
		return result;
	}
	private void creaGrantDefault( OauthClientGrant grant ) throws CommonDbException
	{
		OauthClientDetails det = new OauthClientDetails();
		//Durata token 1 ora
		int tokenDuration = 3600;
		det.setAccessTokenValiditySeconds(tokenDuration);
		det.setAuths(Collections.singletonList(grant));
		det.setAutoApprove(false);
		det.setClientId("esempio");
		det.setClientSecret("esempio");
		det.setCreatoDa("LISTENER");
		det.setDataCreazione(new Date());
		det.setRefreshTokenValiditySeconds(tokenDuration);
		det.setSecretRequired(true);
		det.setScoped(true);
		this.clientDetails.salvaOauthClientDetails(det);
	}
}

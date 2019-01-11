package it.olegna.template.application.commons.services;

import java.util.List;

import it.olegna.template.application.commons.exception.CommonDbException;
import it.olegna.template.application.commons.persistence.models.OauthClientDetails;

/**
 * Interfaccia per le operazioni sugli oggetti di tipo {@link OauthClientDetails}
 */
public interface OauthClientDetailSvc {
	
	/**
	 * Ricerca un {@link OauthClientDetails} in base al suo client id univoco
	 * @param clientId -il client id
	 * @return - l'oggetto trovato
	 * @throws CommonDbException -sollevata in caso di errore
	 */
	OauthClientDetails findByClientId(String clientId) throws CommonDbException;
	/**
	 * Salva una entity di tipo {@link OauthClientDetails}
	 * @param entity - la entity da salvare
	 * @throws CommonDbException -sollevata in caso di errore
	 */
	void salvaOauthClientDetails( OauthClientDetails entity ) throws CommonDbException;

	/**
	 * Salva un {@link List} di entity di tipo {@link OauthClientDetails}
	 * @param entities - le entity da salvare
	 * @throws CommonDbException -sollevata in caso di errore
	 */
	void salvaOauthClientDetails( List<OauthClientDetails> entities ) throws CommonDbException;	
}
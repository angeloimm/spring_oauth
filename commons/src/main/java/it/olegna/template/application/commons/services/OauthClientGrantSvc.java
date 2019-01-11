package it.olegna.template.application.commons.services;
import java.util.List;

import it.olegna.template.application.commons.exception.CommonDbException;
import it.olegna.template.application.commons.persistence.models.OauthClientGrant;

/**
 * Interfaccia per i service dell'oggetto {@link OauthClientGrant}
 *
 */
public interface OauthClientGrantSvc {
	/**
	 * Salva una authority {@link OauthClientGrant}
	 * @param auth -l'oggetto da salvare
	 * @throws CommonDbException -sollevata in caso di errore
	 */
	void salvaOauthClientGrant( OauthClientGrant auth ) throws CommonDbException;
	/**
	 * Salva un {@link List} di {@link OauthClientGrant}
	 * @param auths -gli oggetti da salvare
	 * @throws CommonDbException -sollevata in caso di errore
	 */
	void salvaListaOauthClientGrant( List<OauthClientGrant> auths ) throws CommonDbException;
	/**
	 * Conta il numero di {@link OauthClientGrant}
	 * @return -il numero di oggetti trovati
	 * @throws CommonDbException -sollevata in caso di errore
	 */
	Long contaOauthClientGrant() throws CommonDbException;
	/**
	 * Effettua la ricerca in base al nome grant di {@link OauthClientGrant}
	 * @param name -il nome
	 * @return -l'oggetto trovato o nulla
	 * @throws CommonDbException -sollevata in caso di errore
	 */
	OauthClientGrant findByName(String name) throws CommonDbException;
}
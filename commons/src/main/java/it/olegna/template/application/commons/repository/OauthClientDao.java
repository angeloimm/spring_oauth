package it.olegna.template.application.commons.repository;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import it.olegna.template.application.commons.persistence.models.OauthClientDetails;
import it.olegna.template.application.commons.persistence.models.Operatore;
/**
 * DAO per le operazioni sulle entità di tipo {@link OauthClientDetails}
 *
 */
@Repository
public class OauthClientDao extends AbstractDao<String, OauthClientDetails> {
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<OauthClientDetails> getPersistentClass() {
		return OauthClientDetails.class;
	}
	/**
	 * Effettua la ricerca di un {@link OauthClientDetails} in base alla username
	 * @param clientId -la username dell'operatore
	 * @return - {@link Operatore} trovato
	 */
	public OauthClientDetails findByClientId( String clientId ) {
		CriteriaBuilder cb = createCriteriaBuilder();
		CriteriaQuery<OauthClientDetails> query = cb.createQuery(OauthClientDetails.class);
		Root<OauthClientDetails> root = query.from(OauthClientDetails.class);
		root.fetch("auths", JoinType.INNER);
		query.select(root);
		query.where(cb.equal(root.get("clientId"), clientId));
		OauthClientDetails result = getSession().createQuery(query).getSingleResult();
		return result;
	}
}
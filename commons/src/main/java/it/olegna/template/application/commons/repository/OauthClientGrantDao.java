package it.olegna.template.application.commons.repository;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import it.olegna.template.application.commons.persistence.models.OauthClientGrant;

/**
 * DAO per le operazioni sulle entity di tipo {@link OauthClientGrant}
 *
 */
@Repository
public class OauthClientGrantDao extends AbstractDao<String, OauthClientGrant> {
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<OauthClientGrant> getPersistentClass() {
		return OauthClientGrant.class;
	}
	/**
	 * Effettu la ricerca in base al nome grant di {@link OauthClientGrant}
	 * @param name -il nome del grant
	 * @return -l'oggetto {@link OauthClientGrant} trovato
	 */
	public OauthClientGrant findByName(String name) {
		CriteriaBuilder cb = createCriteriaBuilder();
		CriteriaQuery<OauthClientGrant> query = cb.createQuery(OauthClientGrant.class);
		Root<OauthClientGrant> root = query.from(OauthClientGrant.class);
		query.select(root);
		query.where(cb.equal(root.get("name"), name));
		OauthClientGrant result = getSession().createQuery(query).getSingleResult();
		return result;
	}
}
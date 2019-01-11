package it.olegna.template.application.commons.repository;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import it.olegna.template.application.commons.persistence.models.RuoloOperatore;

/**
 * DAO per le operazioni sulle entity di tipo {@link RuoloOperatore}
 *
 */
@Repository
public class RuoloOperatoreDao extends AbstractDao<String, RuoloOperatore> {
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<RuoloOperatore> getPersistentClass() {
		return RuoloOperatore.class;
	}
	public RuoloOperatore findByName( String name )
	{
		CriteriaBuilder cb = createCriteriaBuilder();
		CriteriaQuery<RuoloOperatore> query = cb.createQuery(RuoloOperatore.class);
		Root<RuoloOperatore> root = query.from(RuoloOperatore.class);
		query.select(root);
		query.where(cb.equal(root.get("name"), name));
		RuoloOperatore result = getSession().createQuery(query).getSingleResult();
		return result;
	}
}
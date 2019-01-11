package it.olegna.template.application.commons.repository;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import it.olegna.template.application.commons.persistence.models.Operatore;

/**
 * DAO per le operazioni sulle entità di tipo {@link Operatore}
 *
 */
@Repository
public class OperatoreDao extends AbstractDao<String, Operatore> {
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<Operatore> getPersistentClass() {
		return Operatore.class;
	}
	/**
	 * Effettua la ricerca di un {@link Operatore} in base alla username
	 * @param username -la username dell'operatore
	 * @return - {@link Operatore} trovato
	 */
	public Operatore findByUsername( String username ) {
		CriteriaBuilder cb = createCriteriaBuilder();
		CriteriaQuery<Operatore> query = cb.createQuery(Operatore.class);
		Root<Operatore> root = query.from(Operatore.class);
		root.fetch("authorities", JoinType.LEFT);
		query.select(root);
		query.where(cb.equal(root.get("username"), username), 
					cb.equal(root.get("accountExpired"), false),
					cb.equal(root.get("accountLocked"), false),
					cb.equal(root.get("credentialsExpired"), false),
					cb.equal(root.get("enabled"), true));
		Operatore result = getSession().createQuery(query).getSingleResult();
		return result;
	}
}
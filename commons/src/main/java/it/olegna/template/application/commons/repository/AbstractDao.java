package it.olegna.template.application.commons.repository;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import it.olegna.template.application.commons.persistence.models.AbstractModel;
/**
 * DAO Generico che mette a disposizione tutti i metodi comuni che possono essere 
 * riutilizzati dalle implementazioni concrete
 *
 * @param <PK> -la primary key della entity
 * @param <T>  -la entity
 */
public abstract class AbstractDao<PK extends Serializable, T extends AbstractModel> {
	/**
	 * Il logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(AbstractDao.class.getName());
	/**
	 * La {@link SessionFactory}
	 */
	@Autowired
	private SessionFactory sf;
	/**
	 * Utilizzata nelle operazioni di bulk per inserimenti massivi
	 */
	@Value("${template.db.hibernate.batch.size}")
	private int dimensioneBatch;
	/**
	 * Metodo astratto che restituisce la {@link Class} rappresentante {@link AbstractModel} gestito
	 * @return la {@link Class} persistente
	 */
	protected abstract Class<T> getPersistentClass();
	/**
	 * Restituisce la {@link Session} corrente di hibernate
	 * @return - {@link Session} corrente
	 */
	protected Session getSession()
	{
		return sf.getCurrentSession();
	}
	/**
	 * Costruisce il {@link CriteriaBuilder} utilizzato per la costruzione delle query 
	 * @return -il {@link CriteriaBuilder} generato
	 */
	protected CriteriaBuilder createCriteriaBuilder()
	{
		return getSession().getCriteriaBuilder();
	}
	/**
	 * Restituisce tutti gli ogget presenti sul DB
	 * @return -il {@link List} di oggetti recuperati
	 */
	public List<T> findAll()
	{
		CriteriaBuilder cb = createCriteriaBuilder();
		CriteriaQuery<T> rootQery = cb.createQuery(getPersistentClass());
		Root<T> rootEntry = rootQery.from(getPersistentClass());
		CriteriaQuery<T> all = rootQery.select(rootEntry);
		TypedQuery<T> allQuery = getSession().createQuery(all);
		return allQuery.getResultList();
	}
	/**
	 * Effettua una count di tutti gli oggetti
	 * @return -il numero di oggetti trovati
	 */
	public Long count()
	{
		CriteriaBuilder qb = createCriteriaBuilder();
		CriteriaQuery<Long> cq = qb.createQuery(Long.class);
		cq.select(qb.count(cq.from(getPersistentClass())));
		return getSession().createQuery(cq).getSingleResult();
	}
	/**
	 * Effettua la count degli oggetti in base alle restrizioni indicate nel {@link DetachedCriteria} in ingresso.
	 * <b>NOTA</b> al {@link DetachedCriteria} in ingresso viene aggiunta la {@link Projection} per il conteggio delle righe
	 * @param dc -Le restrizioni da rispettare
	 * @return -il numero di oggetti trovati
	 */
	public Long count(DetachedCriteria dc) {
		if( logger.isDebugEnabled() )
		{
			logger.debug("Aggiungo projection rowcount");
		}
		dc.setProjection(Projections.rowCount());
		Session session = getSession();
		Number numb = (Number)dc.getExecutableCriteria(session).uniqueResult();
		return numb.longValue();
	}
	/**
	 * Il get effettua la query su DB
	 * @param key -la primary key
	 * @return -l'oggetto recuperato da DB
	 */
	public T getByKey(PK key)
	{
		return (T) getSession().get(getPersistentClass(), key);
	}
	/**
	 * Il load non effettua la query su DB
	 * @param key -la primary key
	 * @return -l'oggetto proxato da DB
	 */
	public T loadByKey(PK key)
	{
		return (T) getSession().load(getPersistentClass(), key);
	}
	/**
	 * Persiste la entity a DB
	 * @param entity -la entity da salvare
	 */
	public void persist(T entity)
	{
		getSession().persist(entity);
	}
	/**
	 * Persiste la entity ed effettua il flush della {@link Session}
	 * @param entity -la entity
	 */
	public void persistFlush(T entity)
	{
		getSession().persist(entity);
		getSession().flush();
	}
	/**
	 * Effettua l'update della entity in ingresso
	 * @param entity
	 */
	public void update(T entity)
	{
		getSession().update(entity);
	}
	/**
	 * Effettua la cancellazione della entity
	 * @param entity
	 */
	public void delete(T entity)
	{
		getSession().delete(entity);
	}
	/**
	 * Effettua l'inserimento massivo di un {@link List} di entities
	 * @param entities
	 */
	public void persist(List<T> entities)
	{
		Session sessione = getSession();
		int i = 0;
		for (T t : entities)
		{
			sessione.persist(t);
			i++;
			if( i%dimensioneBatch == 0 )
			{
				sessione.flush();
				sessione.clear();
			}
		}
	}
	/**
	 * Effettua la ricerca delle entities in base ai criteri indicati dal {@link DetachedCriteria}
	 * @param dc -il {@link DetachedCriteria}
	 * @param offset - l'eventuale offset da cui partire. La query sarà paginata solo se >= 0
	 * @param maxRecordNum -il numero massimo di record. La query sarà paginata solo se > 0
	 * @return il {@link List} di entities
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByDetachedCriteria(DetachedCriteria dc, int offset, int maxRecordNum) {
		if( logger.isDebugEnabled() )
		{
			logger.debug("DETACHED CRITERIA [{}] OFFSET [{}] NUMERO MASSIMO DI RECORD [{}]", dc, offset, maxRecordNum);
		}
		Criteria criteria = dc.getExecutableCriteria(getSession());
		if( offset >= 0 && maxRecordNum > 0 )
		{
			criteria.setFirstResult(offset);
			criteria.setMaxResults(maxRecordNum);
		}
		return criteria.list();
	}

	public Integer executeJpqlStatement(String jpql, Map<String, Object> jpqlParams) {
		if( !StringUtils.hasText(jpql) )
		{
			throw new IllegalArgumentException("Impossibile eseguire lo statement JPQL; query nulla o vuota <"+jpql+">");
		}
		if( logger.isDebugEnabled() )
		{
			logger.debug("ESECUZIONE JPQL [{}] con PARAMETRI [{}]", jpql, jpqlParams);
		}
		Query q = getSession().createQuery(jpql);
		if( jpqlParams != null && !jpqlParams.isEmpty() )
		{
			jpqlParams.keySet().stream().forEach(key ->{
				q.setParameter(key, jpqlParams.get(key));
			});
		}
		int result = q.executeUpdate();
		return result;
	}
}
package it.olegna.template.application.commons.services.impl;
import java.util.List;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import it.olegna.template.application.commons.exception.CommonDbException;
import it.olegna.template.application.commons.persistence.models.OauthClientGrant;
import it.olegna.template.application.commons.repository.OauthClientGrantDao;
import it.olegna.template.application.commons.services.OauthClientGrantSvc;

/**
 * Implementazione della interfaccia {@link OauthClientGrantSvc}
 *
 */
@Service
public class OauthClientGrantSvcImpl implements OauthClientGrantSvc {
	private static final Logger logger = LoggerFactory.getLogger(OauthClientGrantSvcImpl.class.getName());
	@Autowired
	private OauthClientGrantDao oauthClientGrantDao;
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(transactionManager = "hibernateTxMgr", rollbackFor = CommonDbException.class, readOnly = false) 
	public void salvaOauthClientGrant(OauthClientGrant auth) throws CommonDbException {
		
		try {
			oauthClientGrantDao.persist(auth);
		} catch (Exception e) {
			String msg = "Errore nel salvataggio del grant client; "+e.getMessage();
			logger.error(msg, e);
			throw new CommonDbException(msg, e);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(transactionManager = "hibernateTxMgr", rollbackFor = CommonDbException.class, readOnly = false) 
	public void salvaListaOauthClientGrant(List<OauthClientGrant> auths) throws CommonDbException {
		try {
			oauthClientGrantDao.persist(auths);
		} catch (Exception e) {
			String msg = "Errore nel salvataggio dei grant client; "+e.getMessage();
			logger.error(msg, e);
			throw new CommonDbException(msg, e);
		}

	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(transactionManager = "hibernateTxMgr", rollbackFor = CommonDbException.class, readOnly = true) 
	public Long contaOauthClientGrant() throws CommonDbException {
		try {
			return this.oauthClientGrantDao.count();
		} catch (Exception e) {
			String msg = "Errore nel conteggio delle authorities; "+e.getMessage();
			logger.error(msg, e);
			throw new CommonDbException(msg, e);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(transactionManager = "hibernateTxMgr", rollbackFor = CommonDbException.class, readOnly = true) 
		public OauthClientGrant findByName(String name) throws CommonDbException {
		if( !StringUtils.hasText(name) )
		{
			throw new IllegalArgumentException("Passato un nome grant nullo o vuoto. Impossibile continuare");
		}
		try {
			return this.oauthClientGrantDao.findByName(name);
		}
		catch (NoResultException nre) {
			
			logger.warn("Nessun grant trovato con nome {}", name);
			return null;
		} catch (Exception e) {
			String msg = "Errore nel conteggio delle authorities; "+e.getMessage();
			logger.error(msg, e);
			throw new CommonDbException(msg, e);
		}
	}
}
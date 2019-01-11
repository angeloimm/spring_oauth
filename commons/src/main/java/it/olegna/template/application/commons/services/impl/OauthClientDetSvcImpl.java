package it.olegna.template.application.commons.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.olegna.template.application.commons.exception.CommonDbException;
import it.olegna.template.application.commons.persistence.models.OauthClientDetails;
import it.olegna.template.application.commons.repository.OauthClientDao;
import it.olegna.template.application.commons.services.OauthClientDetailSvc;

@Service
public class OauthClientDetSvcImpl implements OauthClientDetailSvc {
	private static final Logger logger = LoggerFactory.getLogger(OauthClientDetSvcImpl.class.getName());
	@Autowired
	private OauthClientDao dao;
	@Autowired
	private PasswordEncoder enc;
	@Override
	@Transactional(transactionManager = "hibernateTxMgr", rollbackFor = CommonDbException.class, readOnly = true) 
	public OauthClientDetails findByClientId(String clientId) throws CommonDbException {
		try
		{
			return dao.findByClientId(clientId);
		}
		catch (Exception e) {
			String message = "Errore nel recupero client con ID "+clientId+"; "+e.getMessage();
			logger.error(message, e);
			throw new CommonDbException(message, e);
		}
	}

	@Override
	@Transactional(transactionManager = "hibernateTxMgr", rollbackFor = CommonDbException.class, readOnly = false) 
	public void salvaOauthClientDetails(OauthClientDetails entity) throws CommonDbException {
		try
		{
			entity.setClientSecret(enc.encode(entity.getClientSecret()));
			dao.persist(entity);
		}
		catch (Exception e) {
			String message = "Errore nel salvataggio client; "+e.getMessage();
			logger.error(message, e);
			throw new CommonDbException(message, e);
		}

	}

	@Override
	public void salvaOauthClientDetails(List<OauthClientDetails> entities) throws CommonDbException {
		try
		{
			entities.forEach(entity -> {
				entity.setClientSecret(enc.encode(entity.getClientSecret()));
			});
			dao.persist(entities);
		}
		catch (Exception e) {
			String message = "Errore nel salvataggio client; "+e.getMessage();
			logger.error(message, e);
			throw new CommonDbException(message, e);
		}

	}

}

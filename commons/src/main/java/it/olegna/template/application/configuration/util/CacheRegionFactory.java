package it.olegna.template.application.configuration.util;
import javax.cache.Cache;

import org.hibernate.cache.jcache.internal.JCacheRegionFactory;

public class CacheRegionFactory extends JCacheRegionFactory {
 static final long serialVersionUID = 1561623459943202900L;

	@Override
	protected Cache<Object, Object> createCache(String regionName) {
		throw new IllegalArgumentException("Cache di hibernate non conosciuta: " + regionName);
	}
}
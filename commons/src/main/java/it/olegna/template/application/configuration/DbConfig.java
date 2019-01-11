package it.olegna.template.application.configuration;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import it.olegna.template.application.configuration.util.CacheRegionFactory;

@Configuration
@EnableTransactionManagement
@PropertySource(value= {"classpath:commons.properties"}, encoding="UTF-8", ignoreResourceNotFound=false)
@Import(value= {DbCacheConfig.class, PwdEncoders.class})
@ComponentScan(basePackages= {"it.olegna.template.application.commons"})
public class DbConfig {
	private static final Logger logger = LoggerFactory.getLogger(DbConfig.class.getName());
	@Autowired
	private Environment env;
	private Properties hibProps()
	{
		Properties props = new Properties();

		props.put(org.hibernate.cfg.Environment.DIALECT, env.getProperty("template.db.hibernate.dialect"));
		props.put(org.hibernate.cfg.Environment.SHOW_SQL, Boolean.valueOf(env.getProperty("template.db.hibernate.show.sql")));
		props.put(org.hibernate.cfg.Environment.GENERATE_STATISTICS, Boolean.valueOf(env.getProperty("template.db.hibernate.generate.statistics")));
		props.put(org.hibernate.cfg.Environment.FORMAT_SQL, Boolean.valueOf(env.getProperty("template.db.hibernate.format.sql")));
		props.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, env.getProperty("template.db.hibernate.ddl.auto"));
		props.put(org.hibernate.cfg.Environment.USE_SECOND_LEVEL_CACHE, Boolean.valueOf(env.getProperty("template.db.hibernate.use.second.cache")));
		props.put(org.hibernate.cfg.Environment.USE_QUERY_CACHE, Boolean.valueOf(env.getProperty("template.db.hibernate.use.query.cache")));
		props.put(org.hibernate.cfg.Environment.CACHE_REGION_FACTORY, CacheRegionFactory.class.getName());
		props.put(org.hibernate.cfg.Environment.STATEMENT_BATCH_SIZE, Integer.valueOf(env.getProperty("template.db.hibernate.batch.size")));
		props.put("hibernate.javax.cache.provider", "org.ehcache.jsr107.EhcacheCachingProvider");
		if( logger.isDebugEnabled() )
		{
			logger.debug("Valorizzate hibernate properties {}", props);
		}
		return props;
	}
	@Bean(initMethod="start",destroyMethod="stop")
	public org.h2.tools.Server h2WebConsonleServer () throws SQLException {
		return org.h2.tools.Server.createWebServer("-web","-webAllowOthers","-webDaemon","-webPort", "8082");
	}
	@Bean(name="hikariDs", destroyMethod = "close")
	public DataSource hikariDataSource(){
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(env.getProperty("template.db.driverClassName"));
		hikariConfig.setJdbcUrl(env.getProperty("template.db.jdbcUrl"));
		hikariConfig.setUsername(env.getProperty("template.db.username"));
		hikariConfig.setPassword(env.getProperty("template.db.password"));
		hikariConfig.setMaximumPoolSize(Integer.parseInt(env.getProperty("template.db.maxPoolSize")));
		hikariConfig.setConnectionTestQuery(env.getProperty("template.db.validationQuery"));
		hikariConfig.setPoolName("Oauth DB Pool");
		hikariConfig.setIdleTimeout(Integer.parseInt(env.getProperty("template.db.maxIdleTime")));
		hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", "true");
		hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
		hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
		hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", "true");
		HikariDataSource dataSource = new HikariDataSource(hikariConfig);
		return dataSource;
	}

	@Bean
	@DependsOn("cacheManager")
	public LocalSessionFactoryBean sessionFactory()
	{
		LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();
		lsfb.setPackagesToScan(new String[]{"it.olegna.template.application.commons.persistence.models"});
		lsfb.setDataSource(this.hikariDataSource());
		lsfb.setHibernateProperties(hibProps());
		return lsfb;
	}
	@Bean(name="hibernateTxMgr")
	public PlatformTransactionManager transactionManager(@Autowired SessionFactory s){
		HibernateTransactionManager result = new HibernateTransactionManager();
		result.setSessionFactory(s);
		return result;
	}
}
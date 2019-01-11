package it.olegna.template.application.configuration;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.ssl.KeyMaterial;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.olegna.template.application.configuration.util.KeyStoreInfo;
import it.olegna.template.application.configuration.util.ProxyRoutePlanner;
import it.olegna.template.application.configuration.util.WsKeepAliveStrategy;

@Configuration
public class HttpConfig {
	private static final Logger logger = LoggerFactory.getLogger(HttpConfig.class.getName());
	@Autowired
	private Environment env;
	
	public HttpConfig() {
		super();
	}

	@Bean("resSvrRestTemplate")
	public RestTemplate restTemplate(@Autowired @Qualifier("bufferingRequestFactory") BufferingClientHttpRequestFactory factory)
	{
		RestTemplate result = new RestTemplate(factory);
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
		result.setInterceptors(interceptors);
		return result;
	}

	@Bean(name= {"httpClientRequestFactory"})
	public ClientHttpRequestFactory requestFactory(@Autowired @Qualifier("httpClient") HttpClient httpClient) throws Exception
	{
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setHttpClient(httpClient);
		return factory;
	}

	@Bean(name= {"bufferingRequestFactory"})
	public BufferingClientHttpRequestFactory bufferingRequestFactory(@Autowired @Qualifier("httpClientRequestFactory") ClientHttpRequestFactory reqFact) throws Exception
	{
		return new BufferingClientHttpRequestFactory(reqFact);
	}

	@Bean(name= {"httpClient"})
	public HttpClient httpClient() throws Exception
	{
		int timeout = Integer.parseInt(env.getProperty("templating.web.http.client.timeout"));
		
		CloseableHttpClient httpClient = null;
		String keystores = env.getProperty("templating.web.certificate");
		PoolingHttpClientConnectionManager pcm = null;
		if (StringUtils.hasText(keystores))
		{
			ObjectMapper objectMapper = new ObjectMapper();
			Resource jsonRes = new ClassPathResource(keystores);
			if (jsonRes.exists())
			{

				List<KeyStoreInfo> ksInfo = objectMapper.readValue(jsonRes.getInputStream(), new TypeReference<List<KeyStoreInfo>>()
				{
				});
				SSLContext sslCtx = SSLContext.getInstance("TLS");
				List<KeyManager> keymanagers = new ArrayList<KeyManager>();
				for (KeyStoreInfo ksi : ksInfo)
				{
					String keystoreName = ksi.getNomeKeyStore();
					String keyStorePwd = ksi.getPasswordKeyStore();
					if (StringUtils.hasText(keystoreName))
					{
						Resource keystoreRes = new ClassPathResource(keystoreName);
						KeyMaterial km = new KeyMaterial(keystoreRes.getInputStream(), keyStorePwd != null ? keyStorePwd.toCharArray() : null);
						KeyStore clientStore = km.getKeyStore();
						KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
						kmfactory.init(clientStore, keyStorePwd != null ? keyStorePwd.toCharArray() : null);
						keymanagers.addAll(Arrays.asList(kmfactory.getKeyManagers()));
					}
				}
				if (!keymanagers.isEmpty())
				{
					// Crediamo a tutti
					X509TrustManager tm = new X509TrustManager()
					{

						@Override
						public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException
						{

						}

						@Override
						public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException
						{

						}

						@Override
						public java.security.cert.X509Certificate[] getAcceptedIssuers()
						{

							return null;
						}

					};
					sslCtx.init(keymanagers.toArray(new KeyManager[keymanagers.size()]), new TrustManager[]
							{ tm }, null);
					SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslCtx);
					Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create().register("https", sslConnectionFactory).register("http", new PlainConnectionSocketFactory()).build();
					pcm = new PoolingHttpClientConnectionManager(registry);
				}
				else
				{
					if (logger.isInfoEnabled())
					{
						logger.info("Nessun keystore presente nel JSON di configurazione {}. Creo un PoolingHttpClientConnectionManager di default", keystores);
					}
					pcm = new PoolingHttpClientConnectionManager();
				}
			}
		}
		else
		{
			if (logger.isInfoEnabled())
			{
				logger.info("Nessun keystore da caricare. Creo un PoolingHttpClientConnectionManager di default");
			}
			pcm = new PoolingHttpClientConnectionManager();
		}
		HttpClientBuilder hcb = HttpClientBuilder.create();
		pcm.closeIdleConnections(timeout, TimeUnit.MILLISECONDS);
		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).setConnectTimeout(timeout).build();
		hcb.setDefaultRequestConfig(config);
		hcb.setConnectionManager(pcm).setConnectionManagerShared(true);
		boolean proxyEnable = Boolean.valueOf(env.getProperty("templating.web.http.client.proxyEnable"));
		if (proxyEnable)
		{
			Integer proxyPort = Integer.valueOf(env.getProperty("templating.web.http.client.portProxy"));
			String proxyHost = env.getProperty("templating.web.http.client.hostProxy");
			BasicCredentialsProvider credentialProvider = new BasicCredentialsProvider();
			AuthScope scope = new AuthScope(proxyHost, proxyPort.intValue());
			String usernameProxy = env.getProperty("templating.web.http.client.usernameProxy");
			String passwordProxy = env.getProperty("templating.web.http.client.passwordProxy");
			if (StringUtils.hasText(usernameProxy) && StringUtils.hasText(passwordProxy))
			{

				UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(usernameProxy, passwordProxy);
				credentialProvider.setCredentials(scope, credentials);
			}
			ProxyRoutePlanner proxyRoutPlanner = new ProxyRoutePlanner(new HttpHost(proxyHost, proxyPort.intValue()), env.getProperty("templating.web.http.client.urlNotProxy"));
			hcb.setDefaultCredentialsProvider(credentialProvider).setRoutePlanner(proxyRoutPlanner);
		}
		WsKeepAliveStrategy cas = new WsKeepAliveStrategy();
		cas.setTimeout(Long.valueOf(timeout));
		hcb.setKeepAliveStrategy(cas);
		hcb.setConnectionManagerShared(true);
		httpClient = hcb.build();
		return httpClient;
	}
}

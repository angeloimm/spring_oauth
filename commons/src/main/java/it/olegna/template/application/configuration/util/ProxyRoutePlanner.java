package it.olegna.template.application.configuration.util;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.protocol.HttpContext;
import org.springframework.util.StringUtils;

public class ProxyRoutePlanner extends DefaultProxyRoutePlanner
{

	private List<String> urlsNotProxy = null;
	private boolean useAlwaysSuper = false;

	public ProxyRoutePlanner(HttpHost proxy, String urlNotProxy)
	{
		super(proxy);
		if (!StringUtils.hasText(urlNotProxy))
			this.useAlwaysSuper = true;
		else
		{
			this.urlsNotProxy = Arrays.asList(urlNotProxy.split(","));
		}
	}

	@Override
	public HttpRoute determineRoute(HttpHost host, HttpRequest request, HttpContext context) throws HttpException
	{

		String hostname = host.getHostName();
		if (this.useAlwaysSuper || this.urlsNotProxy.contains(hostname) == false)
			return super.determineRoute(host, request, context);// Super method
		// with proxy
		if ("http".equals(host.getSchemeName()))
			return new HttpRoute(host);// Direct Route
		HttpClientContext clientContext = HttpClientContext.adapt(context);
		RequestConfig config = clientContext.getRequestConfig();
		InetAddress local = config.getLocalAddress();
		return new HttpRoute(host, local, true);
	}

}
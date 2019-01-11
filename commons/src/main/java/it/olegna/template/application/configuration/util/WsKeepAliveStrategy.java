package it.olegna.template.application.configuration.util;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.protocol.HttpContext;

public class WsKeepAliveStrategy implements ConnectionKeepAliveStrategy
{
	private Long timeout;

	@Override
	public long getKeepAliveDuration(HttpResponse response, HttpContext context)
	{
		return timeout;
	}

	public void setTimeout(Long timeout)
	{
		this.timeout = timeout;
	}

}
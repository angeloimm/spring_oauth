package it.olegna.template.application.configuration.util;
import java.io.Serializable;

public class KeyStoreInfo implements Serializable
{

	private static final long serialVersionUID = -8316394283660073019L;
	private String nomeKeyStore;
	private String passwordKeyStore;

	public KeyStoreInfo()
	{
		super();
	}
	public KeyStoreInfo(String nomeKeyStore, String passwordKeyStore)
	{
		super();
		this.nomeKeyStore = nomeKeyStore;
		this.passwordKeyStore = passwordKeyStore;
	}
	public String getNomeKeyStore()
	{
		return nomeKeyStore;
	}
	public void setNomeKeyStore(String nomeKeyStore)
	{
		this.nomeKeyStore = nomeKeyStore;
	}
	public String getPasswordKeyStore()
	{
		return passwordKeyStore;
	}
	public void setPasswordKeyStore(String passwordKeyStore)
	{
		this.passwordKeyStore = passwordKeyStore;
	}

	@Override
	public String toString()
	{
		return "KeyStoreInfo [nomeKeyStore=" + nomeKeyStore + ", passwordKeyStore=" + passwordKeyStore + "]";
	}
}
package it.olegna.template.application.commons.persistence.models;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@DynamicUpdate
@Cache( region = "it.olegna.template.application.commons.persistence.models.OauthClientDetails", 
		usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(	name = "OAUTH_CLIENT", 
		uniqueConstraints = {@UniqueConstraint(columnNames = {"CLIENT_ID"})}, 
		indexes = { @Index(name = "OA_CLIENT_ID_IDX", columnList = "CLIENT_ID")})
public class OauthClientDetails extends AbstractModel {
	
	private static final long serialVersionUID = -8191186855815682819L;
	private String clientId;
	private String jsonResourceIds;
	private String clientSecret;
	private String jsonScope;
	private String jsonAuthorizedGrantTypes;
	private String jsonRedirectUri;
	private Collection<OauthClientGrant> auths;
	private Integer accessTokenValiditySeconds;
	private Integer refreshTokenValiditySeconds;
	private String jsonAdditionalInformation;
	private boolean autoApprove;
	private boolean secretRequired;
	private boolean scoped;
	@Transient
	private ObjectMapper mapper;
	
	public OauthClientDetails() {
		super();
		mapper = new ObjectMapper();
	}
	@Column(name = "CLIENT_ID")
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	@Column(name = "JSON_RESOURCE_IDS", length=2500)
	public String getJsonResourceIds() {
		return jsonResourceIds;
	}
	public void setJsonResourceIds(String jsonResourceIds) {
		this.jsonResourceIds = jsonResourceIds;
	}
	@Column(name = "CLIENT_SECRET", length=2500)
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	@Column(name = "JSON_SCOPE", length=2500)
	public String getJsonScope() {
		return jsonScope;
	}
	public void setJsonScope(String jsonScope) {
		this.jsonScope = jsonScope;
	}
	@Column(name = "JSON_AUTH_GRANT_TYPE", length=2500)
	public String getJsonAuthorizedGrantTypes() {
		return jsonAuthorizedGrantTypes;
	}
	public void setJsonAuthorizedGrantTypes(String jsonAuthorizedGrantTypes) {
		this.jsonAuthorizedGrantTypes = jsonAuthorizedGrantTypes;
	}
	@Column(name = "JSON_REDIRECT_URI", length=2500)
	public String getJsonRedirectUri() {
		return jsonRedirectUri;
	}
	public void setJsonRedirectUri(String jsonRedirectUri) {
		this.jsonRedirectUri = jsonRedirectUri;
	}
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "OAUTH_CLIENT_OAUTH_GRANT", 
    			joinColumns = @JoinColumn(name = "ID_CLIENT", referencedColumnName = "ID"), 
    			inverseJoinColumns = @JoinColumn(name = "ID_GRANT", referencedColumnName = "ID"))
    @OrderBy
	public Collection<OauthClientGrant> getAuths() {
		return auths;
	}
	public void setAuths(Collection<OauthClientGrant> auths) {
		this.auths = auths;
	}
	@Column(name = "AC_TK_VAL_SEC")
	public Integer getAccessTokenValiditySeconds() {
		return accessTokenValiditySeconds;
	}
	public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}
	@Column(name = "REF_TK_VAL_SEC")
	public Integer getRefreshTokenValiditySeconds() {
		return refreshTokenValiditySeconds;
	}
	public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
	}
	@Column(name = "JSON_ADDITIONAL_INFO", length=2500)
	public String getJsonAdditionalInformation() {
		return jsonAdditionalInformation;
	}
	public void setJsonAdditionalInformation(String jsonAdditionalInformation) {
		this.jsonAdditionalInformation = jsonAdditionalInformation;
	}
	@Column(name = "OAUTH_AUTOAPPROVE")
	public boolean isAutoApprove() {
		return autoApprove;
	}
	public void setAutoApprove(boolean autoApprove) {
		this.autoApprove = autoApprove;
	}
	@Column(name = "SECRET_REQUIRED", length=2500)
	public boolean isSecretRequired() {
		return secretRequired;
	}
	public void setSecretRequired(boolean secretRequired) {
		this.secretRequired = secretRequired;
	}
	@Column(name = "OAUTH_SCOPED", length=2500)
	public boolean isScoped() {
		return scoped;
	}
	public void setScoped(boolean scoped) {
		this.scoped = scoped;
	}
	@Transient
	public Set<String> getResourceIds() {
		Set<String> result = new HashSet<String>();
		if( StringUtils.hasText(this.jsonResourceIds) )
		{
		
			try {
				List<String> elements = this.mapper.readValue(jsonResourceIds, new TypeReference<List<String>>(){});
				result.addAll(elements);
			} catch (IOException e) {
				//Ignoro l'eccezione... non dovrebbe mai capitare
			}
		}
		return result;
	}
	
	@Transient
	public Set<String> getScope() {
		Set<String> result = new HashSet<String>();
		if( StringUtils.hasText(this.jsonScope) )
		{
		
			try {
				List<String> elements = this.mapper.readValue(jsonScope, new TypeReference<List<String>>(){});
				result.addAll(elements);
			} catch (IOException e) {
				//Ignoro l'eccezione... non dovrebbe mai capitare
			}
		}
		return result;
	}
	@Transient
	public Set<String> getAuthorizedGrantTypes() {
		Set<String> result = new HashSet<String>();
		if( StringUtils.hasText(this.jsonAuthorizedGrantTypes) )
		{
		
			try {
				List<String> elements = this.mapper.readValue(jsonAuthorizedGrantTypes, new TypeReference<List<String>>(){});
				result.addAll(elements);
			} catch (IOException e) {
				//Ignoro l'eccezione... non dovrebbe mai capitare
			}
		}
		return result;
	}
	@Transient
	public Set<String> getRegisteredRedirectUri() {
		Set<String> result = new HashSet<String>();
		if( StringUtils.hasText(this.jsonRedirectUri) )
		{
		
			try {
				List<String> elements = this.mapper.readValue(jsonRedirectUri, new TypeReference<List<String>>(){});
				result.addAll(elements);
			} catch (IOException e) {
				//Ignoro l'eccezione... non dovrebbe mai capitare
			}
		}
		return result;
	}
	@Transient
	public boolean isAutoApprove(String scope) {
		
		return getScope().contains(scope);
	}
	@Transient
	public Map<String, Object> getAdditionalInformation() {
		if( StringUtils.hasText(this.jsonRedirectUri) )
		{
		
			try {
				Map<String, Object> result = this.mapper.readValue(jsonRedirectUri, new TypeReference<Map<String, Object>>(){});
				return result;
			} catch (IOException e) {
				//Ignoro l'eccezione... non dovrebbe mai capitare
				
			}
		}
		return new HashMap<>(0);
	}
	
	@Transient
	public Collection<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> result = new ArrayList<>();
		if( !getAuths().isEmpty() )
		{
			getAuths().forEach(auth -> {result.add(auth);});
		}
		return result;
	}
	@Transient
	public ClientDetails toClientDetails()
	{
		BaseClientDetails result = new BaseClientDetails();
		result.setAccessTokenValiditySeconds(this.accessTokenValiditySeconds);
		result.setAdditionalInformation(getAdditionalInformation());
		result.setAuthorities(getAuthorities());
		result.setAuthorizedGrantTypes(getAuthorizedGrantTypes());
		List<String> autoapproveScopes = Collections.singletonList("read");
		result.setAutoApproveScopes(autoapproveScopes);
		result.setClientId(this.getClientId());
		result.setClientSecret(this.getClientSecret());
		result.setRefreshTokenValiditySeconds(this.getRefreshTokenValiditySeconds());
		result.setRegisteredRedirectUri(getRegisteredRedirectUri());
		result.setResourceIds(getResourceIds());
		result.setScope(getScope());
		return result;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((accessTokenValiditySeconds == null) ? 0 : accessTokenValiditySeconds.hashCode());
		result = prime * result + (autoApprove ? 1231 : 1237);
		result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
		result = prime * result + ((clientSecret == null) ? 0 : clientSecret.hashCode());
		result = prime * result + ((jsonAdditionalInformation == null) ? 0 : jsonAdditionalInformation.hashCode());
		result = prime * result + ((jsonAuthorizedGrantTypes == null) ? 0 : jsonAuthorizedGrantTypes.hashCode());
		result = prime * result + ((jsonRedirectUri == null) ? 0 : jsonRedirectUri.hashCode());
		result = prime * result + ((jsonResourceIds == null) ? 0 : jsonResourceIds.hashCode());
		result = prime * result + ((jsonScope == null) ? 0 : jsonScope.hashCode());
		result = prime * result + ((refreshTokenValiditySeconds == null) ? 0 : refreshTokenValiditySeconds.hashCode());
		result = prime * result + (scoped ? 1231 : 1237);
		result = prime * result + (secretRequired ? 1231 : 1237);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		OauthClientDetails other = (OauthClientDetails) obj;
		if (accessTokenValiditySeconds == null) {
			if (other.accessTokenValiditySeconds != null)
				return false;
		} else if (!accessTokenValiditySeconds.equals(other.accessTokenValiditySeconds))
			return false;
		if (autoApprove != other.autoApprove)
			return false;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		if (clientSecret == null) {
			if (other.clientSecret != null)
				return false;
		} else if (!clientSecret.equals(other.clientSecret))
			return false;
		if (jsonAdditionalInformation == null) {
			if (other.jsonAdditionalInformation != null)
				return false;
		} else if (!jsonAdditionalInformation.equals(other.jsonAdditionalInformation))
			return false;
		if (jsonAuthorizedGrantTypes == null) {
			if (other.jsonAuthorizedGrantTypes != null)
				return false;
		} else if (!jsonAuthorizedGrantTypes.equals(other.jsonAuthorizedGrantTypes))
			return false;
		if (jsonRedirectUri == null) {
			if (other.jsonRedirectUri != null)
				return false;
		} else if (!jsonRedirectUri.equals(other.jsonRedirectUri))
			return false;
		if (jsonResourceIds == null) {
			if (other.jsonResourceIds != null)
				return false;
		} else if (!jsonResourceIds.equals(other.jsonResourceIds))
			return false;
		if (jsonScope == null) {
			if (other.jsonScope != null)
				return false;
		} else if (!jsonScope.equals(other.jsonScope))
			return false;
		if (refreshTokenValiditySeconds == null) {
			if (other.refreshTokenValiditySeconds != null)
				return false;
		} else if (!refreshTokenValiditySeconds.equals(other.refreshTokenValiditySeconds))
			return false;
		if (scoped != other.scoped)
			return false;
		if (secretRequired != other.secretRequired)
			return false;
		return true;
	}
}
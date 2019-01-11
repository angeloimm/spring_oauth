package it.olegna.template.application.commons.persistence.models;
import java.util.Collection;

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
import org.springframework.security.core.userdetails.UserDetails;

@DynamicUpdate
@Cache(	region = "it.olegna.template.application.commons.persistence.models.Operatore", 
		usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(	name = "OPERATORE", 
		uniqueConstraints = { @UniqueConstraint(columnNames = {"USERNAME", "EMAIL", "CODICE_FISCALE"}) }, 
		indexes = {@Index(name = "OP_USERNAME_IDX", columnList = "USERNAME") })
public class Operatore extends AbstractModel implements UserDetails {

	private static final long serialVersionUID = -2154137332931276374L;

	private String username;
	private String password;
	private boolean accountExpired;
	private boolean accountLocked;
	private boolean credentialsExpired;
	private boolean enabled;
	private Collection<RuoloOperatore> authorities;
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String email;
	private boolean firstTimeLogin;

	@Column(name = "USERNAME")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "PASSWORD")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "ACCOUNT_EXPIRED")
	public boolean isAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	@Column(name = "ACCOUNT_LOCKED")
	public boolean isAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	@Column(name = "CREDENTIALS_EXPIRED")
	public boolean isCredentialsExpired() {
		return credentialsExpired;
	}

	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	@Column(name = "ENABLED")
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "OPERATORE_RUOLO", joinColumns = @JoinColumn(name = "ID_OPERATORE", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "ID_RUOLO", referencedColumnName = "ID"))
	@OrderBy
	public Collection<RuoloOperatore> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<RuoloOperatore> authorities) {
		this.authorities = authorities;
	}

	@Override
	@Transient
	public boolean isAccountNonExpired() {

		return !isAccountExpired();
	}

	@Override
	@Transient
	public boolean isAccountNonLocked() {

		return !isAccountLocked();
	}

	@Override
	@Transient
	public boolean isCredentialsNonExpired() {

		return !isCredentialsExpired();
	}

	@Column(name = "NOME")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "COGNOME")
	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	@Column(name = "CODICE_FISCALE")
	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "FIRST_TIME_LOGIN")
	public boolean isFirstTimeLogin() {
		return firstTimeLogin;
	}

	public void setFirstTimeLogin(boolean firstTimeLogin) {
		this.firstTimeLogin = firstTimeLogin;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (accountExpired ? 1231 : 1237);
		result = prime * result + (accountLocked ? 1231 : 1237);
		result = prime * result + ((authorities == null) ? 0 : authorities.hashCode());
		result = prime * result + ((codiceFiscale == null) ? 0 : codiceFiscale.hashCode());
		result = prime * result + ((cognome == null) ? 0 : cognome.hashCode());
		result = prime * result + (credentialsExpired ? 1231 : 1237);
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + (firstTimeLogin ? 1231 : 1237);
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
		Operatore other = (Operatore) obj;
		if (firstTimeLogin != other.firstTimeLogin)
			return false;		
		if (accountExpired != other.accountExpired)
			return false;
		if (accountLocked != other.accountLocked)
			return false;
		if (authorities == null) {
			if (other.authorities != null)
				return false;
		} else if (!authorities.equals(other.authorities))
			return false;
		if (codiceFiscale == null) {
			if (other.codiceFiscale != null)
				return false;
		} else if (!codiceFiscale.equals(other.codiceFiscale))
			return false;
		if (cognome == null) {
			if (other.cognome != null)
				return false;
		} else if (!cognome.equals(other.cognome))
			return false;
		if (credentialsExpired != other.credentialsExpired)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (enabled != other.enabled)
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Operatore [username=" + username + ", password=" + password + ", accountExpired=" + accountExpired + ", accountLocked="
				+ accountLocked + ", credentialsExpired=" + credentialsExpired + ", enabled=" + enabled + ", authorities=" + authorities
				+ ", nome=" + nome + ", cognome=" + cognome + ", codiceFiscale=" + codiceFiscale + ", email=" + email + ", firstTimeLogin=" + firstTimeLogin + "]";
	}

}
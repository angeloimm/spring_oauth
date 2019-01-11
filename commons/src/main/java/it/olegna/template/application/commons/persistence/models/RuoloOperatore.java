package it.olegna.template.application.commons.persistence.models;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
@DynamicUpdate
@Cache( region = "it.olegna.template.application.commons.persistence.models.RuoloOperatore", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(	name = "RUOLO_OPERATORE", 	
		uniqueConstraints = {@UniqueConstraint(columnNames = {"NAME"})}, 
		indexes = { @Index(name = "RUOLO_NAME_IDX",	columnList = "NAME")})
public class RuoloOperatore extends AbstractModel implements GrantedAuthority {

	private static final long serialVersionUID = 947587826854724373L;
	private String name;
	private Collection<Operatore> operatori;
	@Transient
	private static final String ROLE_SUFFIX = "ROLE_";
	@Column(name = "NAME", nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "OPERATORE_RUOLO", joinColumns = @JoinColumn(name = "ID_RUOLO", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "ID_OPERATORE", referencedColumnName = "ID"))
	public Collection<Operatore> getOperatori() {
		return operatori;
	}
	public void setOperatori(Collection<Operatore> operatori) {
		this.operatori = operatori;
	}
	@Override
	@Transient
	public String getAuthority() {
		StringBuilder sb = new StringBuilder(getName());
		if( sb.indexOf(ROLE_SUFFIX) == -1 )
		{
			sb.insert(0, ROLE_SUFFIX);
		}
		return sb.toString();
	}	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		RuoloOperatore other = (RuoloOperatore) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "RuoloOperatore [name=" + name + "]";
	}
	
}
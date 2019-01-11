package it.olegna.template.application.commons.persistence.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
public class AbstractModel implements Serializable {

	private static final long serialVersionUID = 2762273919456694374L;
	private String id;
	private String creatoDa;
	private String modificatoDa;
	private Date dataCreazione;
	private Date dataModifica;
	@Id
	@GeneratedValue(generator = "uuid", strategy=GenerationType.IDENTITY)
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "ID", unique = true)
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	@Column(name="CREATO_DA", nullable=false)
	public String getCreatoDa()
	{
		return creatoDa;
	}
	public void setCreatoDa(String creatoDa)
	{
		this.creatoDa = creatoDa;
	}
	@Column(name="MODIFICATO_DA", nullable=true)
	public String getModificatoDa()
	{
		return modificatoDa;
	}
	public void setModificatoDa(String modificatoDa)
	{
		this.modificatoDa = modificatoDa;
	}
	@Column(name="DATA_CREAZIONE", nullable=false)
	public Date getDataCreazione()
	{
		return dataCreazione;
	}
	public void setDataCreazione(Date dataCreazione)
	{
		this.dataCreazione = dataCreazione;
	}
	@Column(name="DATA_MODIFICA", nullable=true)
	public Date getDataModifica()
	{
		return dataModifica;
	}
	public void setDataModifica(Date dataModifica)
	{
		this.dataModifica = dataModifica;
	}
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creatoDa == null) ? 0 : creatoDa.hashCode());
		result = prime * result + ((dataCreazione == null) ? 0 : dataCreazione.hashCode());
		result = prime * result + ((dataModifica == null) ? 0 : dataModifica.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((modificatoDa == null) ? 0 : modificatoDa.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractModel other = (AbstractModel) obj;
		if (creatoDa == null)
		{
			if (other.creatoDa != null)
				return false;
		}
		else if (!creatoDa.equals(other.creatoDa))
			return false;
		if (dataCreazione == null)
		{
			if (other.dataCreazione != null)
				return false;
		}
		else if (!dataCreazione.equals(other.dataCreazione))
			return false;
		if (dataModifica == null)
		{
			if (other.dataModifica != null)
				return false;
		}
		else if (!dataModifica.equals(other.dataModifica))
			return false;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (modificatoDa == null)
		{
			if (other.modificatoDa != null)
				return false;
		}
		else if (!modificatoDa.equals(other.modificatoDa))
			return false;
		return true;
	}
}
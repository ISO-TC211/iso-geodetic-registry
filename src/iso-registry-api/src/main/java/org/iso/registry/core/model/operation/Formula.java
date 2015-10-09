package org.iso.registry.core.model.operation;

import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;

@Access(AccessType.FIELD)
@Audited @Entity
public class Formula extends de.geoinfoffm.registry.core.Entity
{
	@Column(columnDefinition = "text")
	private String formula;
	
	@ManyToOne
	private CI_Citation formulaCitation;

	protected Formula() {
		// TODO Auto-generated constructor stub
	}
	
	protected Formula(UUID uuid) {
		super(uuid);
	}

	public Formula(String formula, CI_Citation formulaCitation) {
		this.formula = formula;
		this.formulaCitation = formulaCitation;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public CI_Citation getFormulaCitation() {
		return formulaCitation;
	}

	public void setFormulaCitation(CI_Citation formulaCitation) {
		this.formulaCitation = formulaCitation;
	}
}

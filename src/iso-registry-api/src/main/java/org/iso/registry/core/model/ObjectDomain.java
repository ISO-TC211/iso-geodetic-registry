package org.iso.registry.core.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.iso19115.extent.ExtentItem;

@Access(AccessType.FIELD)
@Audited
@Embeddable
public class ObjectDomain
{
	@Column(columnDefinition = "text")
	private String scope;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private ExtentItem domainOfValidity;

	protected ObjectDomain() {
	}

	public ObjectDomain(String scope, ExtentItem domainOfValidity) {
		this.setScope(scope);
		this.setDomainOfValidity(domainOfValidity);
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public ExtentItem getDomainOfValidity() {
		return domainOfValidity;
	}

	public void setDomainOfValidity(ExtentItem domainOfValidity) {
		this.domainOfValidity = domainOfValidity;
	}
}

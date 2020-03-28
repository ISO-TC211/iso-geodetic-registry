package org.iso.registry.api.registry.registers.gcp;

import java.util.Objects;

import org.iso.registry.core.model.ObjectDomain;

/**
 * View bean for {@link ObjectDomain}s
 * 
 * @author Florian Esser
 *
 */
public class ObjectDomainViewBean
{
	private String scope;
	private ExtentItemViewBean domainOfValidity;

	public ObjectDomainViewBean() {
	}

	public ObjectDomainViewBean(ObjectDomain domain) {
		Objects.requireNonNull(domain);

		this.scope = domain.getScope();
		this.domainOfValidity = new ExtentItemViewBean(domain.getDomainOfValidity());
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public ExtentItemViewBean getDomainOfValidity() {
		return domainOfValidity;
	}

	public void setDomainOfValidity(ExtentItemViewBean domainOfValidity) {
		this.domainOfValidity = domainOfValidity;
	}
}

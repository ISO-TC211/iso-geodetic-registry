package org.iso.registry.api.registry.registers.gcp;

import java.util.Objects;

import org.iso.registry.core.model.ObjectDomain;

/**
 * Data transfer object for {@link ObjectDomain} proposals
 * 
 * @author Florian Esser
 *
 */
public class ObjectDomainProposalDTO
{
	private String scope;
	private ExtentItemProposalDTO domainOfValidity;

	public ObjectDomainProposalDTO() {
	}

	public ObjectDomainProposalDTO(ObjectDomain domain) {
		Objects.requireNonNull(domain);

		this.scope = domain.getScope();
		this.domainOfValidity = new ExtentItemProposalDTO(domain.getDomainOfValidity());
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public ExtentItemProposalDTO getDomainOfValidity() {
		return domainOfValidity;
	}

	public void setDomainOfValidity(ExtentItemProposalDTO domainOfValidity) {
		this.domainOfValidity = domainOfValidity;
	}
}
